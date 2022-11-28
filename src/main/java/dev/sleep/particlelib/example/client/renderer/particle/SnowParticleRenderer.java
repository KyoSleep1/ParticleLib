package dev.sleep.particlelib.example.client.renderer.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlelib.core.AbstractParticleEmitter;
import dev.sleep.particlelib.core.client.renderer.AbstractParticleRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public class SnowParticleRenderer extends AbstractParticleRenderer {

    @Override
    public void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
    }
}
