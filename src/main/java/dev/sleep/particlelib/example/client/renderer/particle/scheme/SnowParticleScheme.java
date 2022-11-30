package dev.sleep.particlelib.example.client.renderer.particle.scheme;

import dev.sleep.particlelib.Reference;
import dev.sleep.particlelib.core.client.renderer.scheme.AbstractParticleScheme;
import net.minecraft.resources.ResourceLocation;

public class SnowParticleScheme extends AbstractParticleScheme {

    private final ResourceLocation SCHEME_LOCATION = new ResourceLocation(Reference.MODID, "");
    private final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Reference.MODID, "");

    @Override
    public ResourceLocation getSchemeLocation() {
        return SCHEME_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return TEXTURE_LOCATION;
    }
}
