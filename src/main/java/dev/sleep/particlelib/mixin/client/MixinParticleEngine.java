package dev.sleep.particlelib.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlelib.client.ParticleManager;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import dev.sleep.particlelib.client.renderer.ParticleRendererManager;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

    @Inject(method = "tick()V", at = @At(value = "TAIL"))
    public void injectParticleTick(CallbackInfo ci) {
        for (AbstractParticleEmitter particleEmitter : ParticleManager.INSTANCE.PARTICLE_EMITTERS_LIST) {
            final CachedParticleScheme SCHEME = ParticleRendererManager.INSTANCE.getCachedSchemeByEmitterClass(particleEmitter.getClass());
            particleEmitter.tick(SCHEME);
        }
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 0))
    public void injectParticleRenderer(PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks, CallbackInfo ci) {
        ParticleRendererManager.INSTANCE.renderParticles(poseStack, Tesselator.getInstance(), lightTexture, activeRenderInfo, partialTicks);
    }
}
