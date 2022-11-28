package dev.sleep.particlelib.core.impl.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlelib.core.impl.client.ClientParticleManager;
import dev.sleep.particlelib.core.impl.client.renderer.ParticleRendererManager;
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
        ClientParticleManager.INSTANCE.tickParticles(true);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 0))
    public void injectParticleRenderer(PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks, CallbackInfo ci) {
        ParticleRendererManager.INSTANCE.renderParticles(poseStack, buffer, lightTexture, activeRenderInfo, partialTicks);
    }
}
