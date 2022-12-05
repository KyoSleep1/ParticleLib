package dev.sleep.particlecore.client.component.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.sleep.particlecore.EnhancedParticle;
import org.joml.Vector3d;
import software.bernie.geckolib.core.molang.expressions.MolangValue;

public abstract class ShapeDirection {
    public static final ShapeDirection INWARDS = new Inwards(-1);
    public static final ShapeDirection OUTWARDS = new Inwards(1);

    public abstract void applyDirection(EnhancedParticle particle, double x, double y, double z);

    public abstract JsonElement toJson();

    private static class Inwards extends ShapeDirection {
        private final float MUL_FACTOR;

        public Inwards(float factor) {
            this.MUL_FACTOR = factor;
        }

        @Override
        public void applyDirection(EnhancedParticle particle, double x, double y, double z) {
            Vector3d vector = new Vector3d(particle.position);

            vector.sub(new Vector3d(x, y, z));

            if (vector.length() <= 0) {
                vector.set(0, 0, 0);
            } else {
                vector.normalize();
                vector.mul(this.MUL_FACTOR);
            }

            particle.speed.set(vector);
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.MUL_FACTOR < 0 ? "inwards" : "outwards");
        }
    }

    public static class Vector extends ShapeDirection {
        public MolangValue x;
        public MolangValue y;
        public MolangValue z;

        public Vector(MolangValue x, MolangValue y, MolangValue z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void applyDirection(EnhancedParticle particle, double x, double y, double z) {
            particle.speed.set((float) this.x.get(), (float) this.y.get(), (float) this.z.get());

            if (particle.speed.length() <= 0) {
                particle.speed.set(0, 0, 0);
            } else {
                particle.speed.normalize();
            }
        }

        @Override
        public JsonElement toJson() {
            JsonArray array = new JsonArray();
            array.add(this.x.toJson());
            array.add(this.y.toJson());
            array.add(this.z.toJson());

            return array;
        }
    }
}
