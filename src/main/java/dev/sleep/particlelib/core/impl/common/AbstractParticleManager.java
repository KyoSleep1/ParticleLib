package dev.sleep.particlelib.core.impl.common;

import dev.sleep.particlelib.core.AbstractParticleEmitter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class AbstractParticleManager {

    protected final ObjectArrayList<AbstractParticleEmitter> PARTICLE_EMITTERS_LIST = new ObjectArrayList<>();

    public void tickParticles(boolean clientSide) {
        for (AbstractParticleEmitter particleEmitter : PARTICLE_EMITTERS_LIST) {
            if (particleEmitter.isClientSide() != clientSide) {
                continue;
            }

            particleEmitter.tick();
        }
    }

    public void addToList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.add(particleEmitter);
    }

    public void removeFromList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.remove(particleEmitter);
    }
}
