package dev.sleep.particlelib.client.loading.object;

import com.google.common.collect.BiMap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import dev.sleep.particlecore.client.renderer.ComponentRegistry;
import dev.sleep.particlelib.client.loading.object.component.*;
import dev.sleep.particlelib.client.loading.object.type.ParticleMaterialType;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ParticleScheme {

    @Getter
    private String identifier;

    @Getter
    private String formatVersion;

    @Getter
    private ResourceLocation textureLocation;

    @Getter
    private ParticleMaterialType material;

    @Getter
    private final HashMap<String, ParticleCurve> CurvesList = new HashMap<>();

    @Getter
    private final List<AbstractComponent> ComponentsList = new ArrayList<>();

    public static JsonDeserializer<ParticleScheme> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            ParticleScheme particleScheme = new ParticleScheme();
            JsonObject root = json.getAsJsonObject();

            JsonObject effectObject = ParticleScheme.getObject(root, "particle_effect");
            JsonObject descriptionObject = ParticleScheme.getObject(effectObject, "description");

            ParticleScheme.parseDescription(particleScheme, root, descriptionObject);
            ParticleScheme.parseEffect(particleScheme, effectObject);

            return particleScheme;
        };
    }

    private static void parseDescription(ParticleScheme particleScheme, JsonObject root, JsonObject description) throws JsonParseException {
        particleScheme.formatVersion = root.get("format_version").getAsString();

        if (description.has("identifier")) {
            particleScheme.identifier = description.get("identifier").getAsString();
        }

        JsonObject parameters = ParticleScheme.getObject(description, "basic_render_parameters");
        if (parameters.has("material")) {
            particleScheme.material = ParticleMaterialType.fromString(parameters.get("material").getAsString());
        }

        if (parameters.has("texture")) {
            String texture = parameters.get("texture").getAsString();
            particleScheme.textureLocation = new ResourceLocation(texture);
        }
    }

    private static void parseEffect(ParticleScheme particleScheme, JsonObject root) throws JsonParseException {
        if (root.has("curves")) {
            JsonElement curves = root.get("curves");

            if (curves.isJsonObject()) {
                ParticleScheme.parseCurves(particleScheme, curves.getAsJsonObject());
            }
        }

        ParticleScheme.parseComponents(particleScheme, ParticleScheme.getObject(root, "components"));
    }

    /**
     * Parse curves object
     */
    private static void parseCurves(ParticleScheme particleScheme, JsonObject curves) {
        for (Map.Entry<String, JsonElement> entry : curves.entrySet()) {
            JsonElement element = entry.getValue();

            if (element.isJsonObject()) {
                ParticleCurve curve = new ParticleCurve();

                try {
                    curve.fromJson(element.getAsJsonObject());
                } catch (MolangException e) {
                    throw new RuntimeException(e);
                }

                particleScheme.CurvesList.put(entry.getKey(), curve);
            }
        }
    }

    private static void parseComponents(ParticleScheme particleScheme, JsonObject components) {
        BiMap<String, Class<? extends AbstractComponent>> componentsList = ComponentRegistry.getComponentsList();
        for (Map.Entry<String, JsonElement> entry : components.entrySet()) {
            String key = entry.getKey();

            if (componentsList.containsKey(key)) {
                AbstractComponent component = null;

                try {
                    component = componentsList.get(key).getConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (component == null) {
                    return;
                }

                component.fromJson(entry.getValue());
                particleScheme.ComponentsList.add(component);
            }
        }

        for (Map.Entry<String, ParticleCurve> entry : particleScheme.CurvesList.entrySet()) {
            entry.getValue().variable = MolangParser.INSTANCE.variables.get(entry.getKey());
        }
    }

    private static JsonObject getObject(JsonObject object, String key) throws JsonParseException {
        if (!object.has(key)) {
            throw new JsonParseException("Could not find key: " + key + " value");
        }

        JsonElement objectElement = object.get(key);
        if (!objectElement.isJsonObject()) {
            throw new JsonParseException("the specified key " + key + " is not a Object");
        }

        return objectElement.getAsJsonObject();
    }
}
