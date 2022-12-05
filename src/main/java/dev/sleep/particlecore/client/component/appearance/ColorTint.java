package dev.sleep.particlecore.client.component.appearance;

import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.utils.Interpolations;
import com.eliotlash.mclib.utils.MathUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ColorTint {
    /**
     * Parse a single color either in hex string format or JSON array
     * (this should parse both RGB and RGBA expressions)
     */
    public static Solid parseColor(JsonElement element) throws MolangException {
        MolangValue r = MolangParser.ONE, g = MolangParser.ONE, b = MolangParser.ONE, a = MolangParser.ONE;

        if (element.isJsonPrimitive()) {
            String hex = element.getAsString();

            if (hex.startsWith("#") && (hex.length() == 7 || hex.length() == 9)) {
                try {
                    int color = Integer.parseInt(hex.substring(1), 16);
                    float hr = (color >> 16 & 0xff) / 255F;
                    float hg = (color >> 8 & 0xff) / 255F;
                    float hb = (color & 0xff) / 255F;
                    float ha = hex.length() == 9 ? (color >> 24 & 0xff) : 1;

                    r = new MolangValue(new Constant(hr));
                    g = new MolangValue(new Constant(hg));
                    b = new MolangValue(new Constant(hb));
                    a = new MolangValue(new Constant(ha));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();

            if (array.size() == 3 || array.size() == 4) {
                r = MolangParser.parseJson(array.get(0));
                g = MolangParser.parseJson(array.get(1));
                b = MolangParser.parseJson(array.get(2));

                if (array.size() == 4) {
                    a = MolangParser.parseJson(array.get(3));
                }
            }
        }

        return new Solid(r, g, b, a);
    }

    /**
     * Parse a gradient
     */
    public static ColorTint parseGradient(JsonObject color) throws MolangException {
        JsonElement gradient = color.get("gradient");

        MolangValue expression = MolangParser.ZERO;
        List<Gradient.ColorStop> colorStops = new ArrayList<>();
        boolean equal = true;

        if (gradient.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : gradient.getAsJsonObject().entrySet()) {
                colorStops.add(new Gradient.ColorStop(Float.parseFloat(entry.getKey()), parseColor(entry.getValue())));
            }

            colorStops.sort((a, b) -> a.stop > b.stop ? 1 : -1);
            equal = false;
        } else if (gradient.isJsonArray()) {
            JsonArray colors = gradient.getAsJsonArray();

            int i = 0;

            for (JsonElement stop : colors) {
                colorStops.add(new Gradient.ColorStop(i / (float) (colors.size() - 1), parseColor(stop)));

                i++;
            }
        }

        if (color.has("interpolant")) {
            expression = MolangParser.parseJson(color.get("interpolant"));
        }

        return new Gradient(colorStops, expression, equal);
    }

    public abstract JsonElement toJson();

    public abstract void compute(EnhancedParticle particle);

    /**
     * Solid color (not necessarily static)
     */
    public static class Solid extends ColorTint {
        public MolangValue r, g, b, a;

        public Solid(MolangValue r, MolangValue g, MolangValue b, MolangValue a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public boolean isConstant() {
            return this.r.isConstant() && this.g.isConstant() && this.b.isConstant() && this.a.isConstant();
        }

        @Override
        public void compute(EnhancedParticle particle) {
            particle.color.set((float) this.r.get(), (float) this.g.get(), (float) this.b.get(), (float) this.a.get());
        }

        @Override
        public JsonElement toJson() {
            JsonArray array = new JsonArray();

            if (this.r.isOne() && this.g.isOne() && this.b.isOne() && this.a.isOne()) {
                return array;
            }

            array.add(this.r.toJson());
            array.add(this.g.toJson());
            array.add(this.b.toJson());
            array.add(this.a.toJson());

            return array;
        }

        public JsonElement toHexJson() {
            int r = (int) (this.r.get() * 255) & 0xff;
            int g = (int) (this.g.get() * 255) & 0xff;
            int b = (int) (this.b.get() * 255) & 0xff;
            int a = (int) (this.a.get() * 255) & 0xff;

            String hex = "#";

            if (a < 255) {
                hex += StringUtils.leftPad(Integer.toHexString(a), 2, "0").toUpperCase();
            }

            hex += StringUtils.leftPad(Integer.toHexString(r), 2, "0").toUpperCase();
            hex += StringUtils.leftPad(Integer.toHexString(g), 2, "0").toUpperCase();
            hex += StringUtils.leftPad(Integer.toHexString(b), 2, "0").toUpperCase();

            return new JsonPrimitive(hex);
        }

        public void lerp(EnhancedParticle particle, float factor) {
            particle.color.set(Interpolations.lerp(particle.color.x, (float) this.r.get(), factor),
                    Interpolations.lerp(particle.color.y, (float) this.g.get(), factor),
                    Interpolations.lerp(particle.color.z, (float) this.b.get(), factor),
                    Interpolations.lerp(particle.color.w, (float) this.a.get(), factor));
        }
    }

    /**
     * Gradient color, instead of using formulas, you can just specify a couple of colors
     * and an expression at which color it would stop
     */
    public static class Gradient extends ColorTint {

        public List<ColorStop> stops;
        public MolangValue interpolant;
        public boolean equal;

        public Gradient(List<ColorStop> stops, MolangValue interpolant, boolean equal) {
            this.stops = stops;
            this.interpolant = interpolant;
            this.equal = equal;
        }

        @Override
        public void compute(EnhancedParticle particle) {
            int length = this.stops.size();

            if (length == 0) {
                particle.color.x = particle.color.y = particle.color.w = particle.color.z = 1;
                return;
            } else if (length == 1) {
                this.stops.get(0).color.compute(particle);

                return;
            }

            double factor = this.interpolant.get();
            factor = MathUtils.clamp(factor, 0, 1);

            ColorStop prev = this.stops.get(0);
            if (factor < prev.stop) {
                prev.color.compute(particle);

                return;
            }

            for (int i = 1; i < length; i++) {
                ColorStop stop = this.stops.get(i);

                if (stop.stop > factor) {
                    prev.color.compute(particle);
                    stop.color.lerp(particle, (float) (factor - prev.stop) / (stop.stop - prev.stop));

                    return;
                }

                prev = stop;
            }

            prev.color.compute(particle);
        }

        @Override
        public JsonElement toJson() {
            JsonObject object = new JsonObject();
            JsonElement color;

            if (this.equal) {
                JsonArray gradient = new JsonArray();

                for (ColorStop stop : this.stops) {
                    gradient.add(stop.color.toHexJson());
                }

                color = gradient;
            } else {
                JsonObject gradient = new JsonObject();

                for (ColorStop stop : this.stops) {
                    gradient.add(String.valueOf(stop.stop), stop.color.toHexJson());
                }

                color = gradient;
            }

            if (!JsonUtil.isElementEmpty(color)) {
                object.add("gradient", color);
            }

            if (!this.interpolant.isZero()) {
                object.add("interpolant", this.interpolant.toJson());
            }

            return object;
        }

        public static class ColorStop {
            public float stop;
            public Solid color;

            public ColorStop(float stop, Solid color) {
                this.stop = stop;
                this.color = color;
            }
        }
    }
}
