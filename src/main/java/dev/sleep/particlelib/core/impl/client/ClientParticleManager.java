package dev.sleep.particlelib.core.impl.client;

import dev.sleep.particlelib.core.AbstractParticleEmitter;
import dev.sleep.particlelib.core.impl.common.AbstractParticleManager;

public class ClientParticleManager extends AbstractParticleManager {

    public static final ClientParticleManager INSTANCE = new ClientParticleManager();

    @Override
    public void addToList(AbstractParticleEmitter particleEmitter) {
        super.addToList(particleEmitter);
        this.sortList();
    }

    @Override
    public void removeFromList(AbstractParticleEmitter particleEmitter) {
        super.removeFromList(particleEmitter);
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


    public void fetchList(ParticleListCallback callback) {
        for (AbstractParticleEmitter particleEmitter : PARTICLE_EMITTERS_LIST) {
            callback.OnParticleListFetch(particleEmitter);
        }
    }

    public interface ParticleListCallback {
        void OnParticleListFetch(AbstractParticleEmitter particleEmitter);
    }
}
