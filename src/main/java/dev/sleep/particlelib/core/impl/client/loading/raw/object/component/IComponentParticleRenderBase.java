package dev.sleep.particlelib.core.impl.client.loading.raw.object.component;

import dev.sleep.particlelib.core.AbstractParticleEmitter;

public interface IComponentParticleRenderBase extends IComponentBase {

	void preRender(AbstractParticleEmitter emitter, float partialTicks);

	void postRender(AbstractParticleEmitter emitter, float partialTicks);

}
