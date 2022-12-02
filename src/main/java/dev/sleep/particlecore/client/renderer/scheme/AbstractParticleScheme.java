package dev.sleep.particlecore.client.renderer.scheme;

import dev.sleep.particlelib.client.loading.LoadingCache;
import dev.sleep.particlelib.client.loading.object.ParticleScheme;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractParticleScheme {

    public abstract ResourceLocation getSchemeLocation();

    public ParticleScheme getParticleScheme() {
        return LoadingCache.getCachedSchemes().get(this.getSchemeLocation());
    }

}
