package dev.sleep.particlelib.example.client.renderer.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.client.renderer.AbstractParticleRenderer;
import dev.sleep.particlelib.example.client.renderer.particle.scheme.SnowParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public class SnowParticleRenderer extends AbstractParticleRenderer {

    public SnowParticleRenderer() {
        super(new SnowParticleScheme());
    }

    @Override
    public void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
    }
}
