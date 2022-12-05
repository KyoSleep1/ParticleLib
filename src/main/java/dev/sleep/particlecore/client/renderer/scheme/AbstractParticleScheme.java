package dev.sleep.particlecore.client.renderer.scheme;

import dev.sleep.particlelib.Main;
import dev.sleep.particlelib.client.loading.LoadingCache;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractParticleScheme {

    public abstract ResourceLocation getSchemeLocation();

    public CachedParticleScheme getCachedScheme() {
        CachedParticleScheme particleScheme = LoadingCache.getCachedSchemes().get(this.getSchemeLocation());
        if (particleScheme == null) {
            Main.warnCritical("Could not find scheme " + this.getClass() + " in the following location: " + this.getSchemeLocation(), null);
        }

        return particleScheme;
    }
}
