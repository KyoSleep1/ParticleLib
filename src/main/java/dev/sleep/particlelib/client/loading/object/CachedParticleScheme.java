package dev.sleep.particlelib.client.loading.object;

import com.google.common.collect.BiMap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.sleep.particlecore.client.component.AbstractComponent;
import software.bernie.geckolib.core.molang.MolangException;
import software.bernie.geckolib.core.molang.MolangParser;
import dev.sleep.particlelib.client.ParticleComponentRegistry;
import dev.sleep.particlelib.client.loading.object.type.ParticleMaterialType;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class CachedParticleScheme {

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

    public static JsonDeserializer<CachedParticleScheme> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            CachedParticleScheme particleScheme = new CachedParticleScheme();
            JsonObject root = json.getAsJsonObject();

            JsonObject effectObject = CachedParticleScheme.getObject(root, "particle_effect");
            JsonObject descriptionObject = CachedParticleScheme.getObject(effectObject, "description");

            CachedParticleScheme.parseDescription(particleScheme, root, descriptionObject);
            CachedParticleScheme.parseEffect(particleScheme, effectObject);

            return particleScheme;
        };
    }

    private static void parseDescription(CachedParticleScheme particleScheme, JsonObject root, JsonObject description) throws JsonParseException {
        particleScheme.formatVersion = root.get("format_version").getAsString();

        if (description.has("identifier")) {
            particleScheme.identifier = description.get("identifier").getAsString();
        }

        JsonObject parameters = CachedParticleScheme.getObject(description, "basic_render_parameters");
        if (parameters.has("material")) {
            particleScheme.material = ParticleMaterialType.fromString(parameters.get("material").getAsString());
        }

        if (parameters.has("texture")) {
            String texture = parameters.get("texture").getAsString();
            particleScheme.textureLocation = new ResourceLocation(texture);
        }
    }

    private static void parseEffect(CachedParticleScheme particleScheme, JsonObject root) throws JsonParseException {
        if (root.has("curves")) {
            JsonElement curves = root.get("curves");

            if (curves.isJsonObject()) {
                CachedParticleScheme.parseCurves(particleScheme, curves.getAsJsonObject());
            }
        }

        try {
            CachedParticleScheme.parseComponents(particleScheme, CachedParticleScheme.getObject(root, "components"));
        } catch (MolangException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse curves object
     */
    private static void parseCurves(CachedParticleScheme particleScheme, JsonObject curves) {
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

    private static void parseComponents(CachedParticleScheme particleScheme, JsonObject components) throws MolangException {
        BiMap<String, Class<? extends AbstractComponent>> componentsList = ParticleComponentRegistry.getComponentsList();
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
