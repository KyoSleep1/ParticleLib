package dev.sleep.particlecore.client.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlecore.client.renderer.scheme.AbstractParticleScheme;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;

public class DefaultedEmitterRenderer extends AbstractEmitterRenderer {

    public DefaultedEmitterRenderer(AbstractParticleScheme particleScheme) {
        super(particleScheme);
    }

    @Override
    public void preRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, BufferBuilder buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        this.beginBuilder(buffer);
        this.setupCameraPropertiesAndSort(particleEmitter, activeRenderInfo);
        for (AbstractComponent preRenderComponent : scheme.getComponentsList()) {
            preRenderComponent.preRender(particleEmitter, partialTicks);
        }
    }

    @Override
    public void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, Tesselator tesselator, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        CachedParticleScheme cachedScheme = ParticleScheme.getCachedScheme();
        this.preRender(particleEmitter, cachedScheme, poseStack, tesselator.getBuilder(), lightTexture, activeRenderInfo, partialTicks);

        for (EnhancedParticle particle : particleEmitter.PARTICLES_LIST) {
            this.setupParticleMatrix(particleEmitter, particle);
            for (AbstractComponent component : cachedScheme.getComponentsList()) {
                component.render(particleEmitter, particle, poseStack, tesselator.getBuilder(), lightTexture, activeRenderInfo, partialTicks);
            }
        }

        this.postRender(particleEmitter, cachedScheme, poseStack, tesselator, lightTexture, activeRenderInfo, partialTicks);
    }

    @Override
    public void postRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, Tesselator tesselator, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        for (AbstractComponent postRenderComponent : scheme.getComponentsList()) {
            postRenderComponent.postRender(particleEmitter, scheme, partialTicks);
        }

        this.endBuilder(tesselator);
    }
}
