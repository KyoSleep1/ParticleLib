package dev.sleep.particlelib.example.common.particle;

import dev.sleep.particlelib.core.AbstractParticleEmitter;

public class SnowParticleEmitter extends AbstractParticleEmitter {

    public SnowParticleEmitter() {
        super(true);
    }

    @Override
    public void tick() {
    }

    @Override
    public double getDistanceSq() {
        return 0;
    }
}
