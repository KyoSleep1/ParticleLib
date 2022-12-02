package dev.sleep.particlelib.client.loading.object.component;

import com.google.gson.JsonElement;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;

public abstract class AbstractComponent {

    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTicks) {
    }

    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTicks) {
    }

    public void preRender(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTicks) {
    }

    public void render(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTicks) {
    }

    public void postRender(AbstractParticleEmitter emitter, EnhancedParticle particle, float partialTicks) {
    }

    public abstract AbstractComponent fromJson(JsonElement element);

    public abstract JsonElement toJson();

    public abstract boolean canBeEmpty();
}
