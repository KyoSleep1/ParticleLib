package dev.sleep.particlecore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlecore.client.renderer.scheme.AbstractParticleScheme;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public class DefaultedEmitterRenderer extends AbstractEmitterRenderer {

    public DefaultedEmitterRenderer(AbstractParticleScheme particleScheme) {
        super(particleScheme);
    }

    @Override
    public void preRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        this.setupCameraPropertiesAndSort(particleEmitter, activeRenderInfo);
        for (AbstractComponent preRenderComponent : scheme.getComponentsList()) {
            preRenderComponent.preRender(particleEmitter, partialTicks);
        }
    }

    @Override
    public void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        CachedParticleScheme cachedScheme = ParticleScheme.getCachedScheme();
        this.preRender(particleEmitter, cachedScheme, poseStack, buffer, lightTexture, activeRenderInfo, partialTicks);

        for (EnhancedParticle particle : particleEmitter.PARTICLES_LIST) {
            this.setupParticleMatrix(particleEmitter, particle);
            for (AbstractComponent component : cachedScheme.getComponentsList()) {
                component.render(particleEmitter, partialTicks);
            }
        }

        this.postRender(particleEmitter, cachedScheme, poseStack, buffer, lightTexture, activeRenderInfo, partialTicks);
    }

    @Override
    public void postRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        for (AbstractComponent postRenderComponent : scheme.getComponentsList()) {
            postRenderComponent.postRender(particleEmitter, scheme, partialTicks);
        }
    }
}
