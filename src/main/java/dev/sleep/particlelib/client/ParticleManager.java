package dev.sleep.particlelib.client;

import dev.sleep.particlecore.AbstractParticleEmitter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ParticleManager {

    public static final ParticleManager INSTANCE = new ParticleManager();
    public final ObjectArrayList<AbstractParticleEmitter> PARTICLE_EMITTERS_LIST = new ObjectArrayList<>();

    public void addToList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.add(particleEmitter);
    }

    public void removeFromList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.remove(particleEmitter);
    }
}
