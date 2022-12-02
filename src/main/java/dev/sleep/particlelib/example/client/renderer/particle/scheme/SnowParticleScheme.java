package dev.sleep.particlelib.example.client.renderer.particle.scheme;

import dev.sleep.particlelib.Reference;
import dev.sleep.particlecore.client.renderer.scheme.AbstractParticleScheme;
import net.minecraft.resources.ResourceLocation;

public class SnowParticleScheme extends AbstractParticleScheme {

    private final ResourceLocation SCHEME_LOCATION = new ResourceLocation(Reference.MODID, "scheme/snow.particle.json");

    @Override
    public ResourceLocation getSchemeLocation() {
        return SCHEME_LOCATION;
    }

}
