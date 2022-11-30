package dev.sleep.particlelib.core.impl.client.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sleep.particlelib.Main;
import dev.sleep.particlelib.core.client.renderer.scheme.AbstractParticleScheme;
import dev.sleep.particlelib.core.impl.client.loading.raw.RawParticleScheme;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

public class SchemeFileLoader {

    public static final Gson SCHEME_JSON = new GsonBuilder().setLenient()
            .registerTypeAdapter(RawParticleScheme.class, RawParticleScheme.deserializer()).create();

    public static RawParticleScheme loadScheme(AbstractParticleScheme scheme) {
        ResourceLocation schemeLocation = scheme.getSchemeLocation();
        String exceptionMessage = "Could not load scheme for: " + scheme.getClass();

        if (schemeLocation == null) {
            Main.warnCritical(exceptionMessage, null);
            return null;
        }

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        Optional<Resource> resource = resourceManager.getResource(schemeLocation);

        if (resource.isEmpty()) {
            Main.warnCritical(exceptionMessage, null);
            return null;
        }

        JsonObject jsonobject = SchemeFileLoader.loadFile(schemeLocation, resourceManager);
        return SchemeFileLoader.getParsedScheme(jsonobject);
    }

    /**
     * Load a given json file into memory
     * @param location The resource path of the json file
     * @param manager The Minecraft {@code ResourceManager} responsible for maintaining in-memory resource access
     */
    private static JsonObject loadFile(ResourceLocation location, ResourceManager manager) {
        return GsonHelper.fromJson(SCHEME_JSON, getFileContents(location, manager), JsonObject.class);
    }

    /**
     * Read a text-based file into memory in the form of a single string
     * @param location The resource path of the file
     * @param manager The Minecraft {@code ResourceManager} responsible for maintaining in-memory resource access
     */
    private static String getFileContents(ResourceLocation location, ResourceManager manager) {
        try (InputStream inputStream = manager.getResourceOrThrow(location).open()) {
            return IOUtils.toString(inputStream, Charset.defaultCharset());
        }
        catch (Exception e) {
            Main.getLogger().error("Couldn't load " + location, e);
            throw new RuntimeException(new FileNotFoundException(location.toString()));
        }
    }

    private static RawParticleScheme getParsedScheme(JsonElement json) {
        return SCHEME_JSON.fromJson(json, RawParticleScheme.class);
    }
}
