package dev.sleep.particlelib.core.impl.client.loading.raw.object.component;

import dev.sleep.particlelib.core.AbstractParticleEmitter;
import dev.sleep.particlelib.core.Particle;

public interface IComponentParticleUpdate extends IComponentBase {

    void update(AbstractParticleEmitter emitter, Particle particle);

}
