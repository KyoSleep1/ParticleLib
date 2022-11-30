package dev.sleep.particlelib.core.client.renderer.scheme;

import dev.sleep.particlelib.core.impl.client.loading.SchemeFileLoader;
import dev.sleep.particlelib.core.impl.client.loading.raw.RawParticleScheme;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractParticleScheme {

    @Getter
    private final RawParticleScheme RawParticleScheme;

    public AbstractParticleScheme() {
        RawParticleScheme = SchemeFileLoader.loadScheme(this);
    }

    public abstract ResourceLocation getSchemeLocation();

    public abstract ResourceLocation getTextureLocation();

}
