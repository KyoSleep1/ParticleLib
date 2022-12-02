package dev.sleep.particlelib.client;

import dev.sleep.particlecore.AbstractParticleEmitter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ParticleManager  {

    public static final ParticleManager INSTANCE = new ParticleManager();
    protected final ObjectArrayList<AbstractParticleEmitter> PARTICLE_EMITTERS_LIST = new ObjectArrayList<>();

    public void addToList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.add(particleEmitter);
        this.sortList();
    }

    public void removeFromList(AbstractParticleEmitter particleEmitter) {
        PARTICLE_EMITTERS_LIST.remove(particleEmitter);
        this.sortList();
    }

    private void sortList() {
        PARTICLE_EMITTERS_LIST.sort((particleA, particleB) -> {
            double particleADistance = particleA.getDistanceSq();
            double particleBDistance = particleB.getDistanceSq();

            if (particleADistance < particleBDistance) {
                return 1;
            } else if (particleADistance > particleBDistance) {
                return -1;
            }

            return 0;
        });
    }

    public void tickParticles() {
        for (AbstractParticleEmitter particleEmitter : PARTICLE_EMITTERS_LIST) {
            particleEmitter.tick();
        }
    }

    public void fetchList(ParticleListCallback callback) {
        for (AbstractParticleEmitter particleEmitter : PARTICLE_EMITTERS_LIST) {
            callback.OnParticleListFetch(particleEmitter);
        }
    }

    public interface ParticleListCallback {
        void OnParticleListFetch(AbstractParticleEmitter particleEmitter);
    }
}
