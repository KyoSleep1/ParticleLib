package dev.sleep.particlecore.client.component.lifetime;

import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;

public class ComponentLifetimeOnce extends ComponentLifetime {

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if (!updateEmitterOnly) {
            return;
        }

        emitter.lifetime = (int) (this.activeTime.get() * 20);
        if (emitter.getAge() >= emitter.lifetime) {
            emitter.stop();
        }
    }
}
