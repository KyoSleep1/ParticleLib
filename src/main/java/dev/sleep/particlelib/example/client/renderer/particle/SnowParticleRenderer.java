package dev.sleep.particlelib.example.client.renderer.particle;

import dev.sleep.particlecore.client.renderer.DefaultedEmitterRenderer;
import dev.sleep.particlelib.example.client.renderer.particle.scheme.SnowParticleScheme;

public class SnowParticleRenderer extends DefaultedEmitterRenderer {

    public SnowParticleRenderer() {
        super(new SnowParticleScheme());
    }
}
