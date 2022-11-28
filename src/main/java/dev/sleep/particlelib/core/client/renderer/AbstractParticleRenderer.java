package dev.sleep.particlelib.core.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlelib.core.AbstractParticleEmitter;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class AbstractParticleRenderer {

    public abstract void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks);

}
