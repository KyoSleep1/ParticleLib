package dev.sleep.particlecore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.client.renderer.scheme.AbstractParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class AbstractParticleRenderer {

    protected final AbstractParticleScheme PARTICLE_SCHEME;

    public AbstractParticleRenderer(AbstractParticleScheme particleScheme){
        this.PARTICLE_SCHEME = particleScheme;
    }

    public abstract void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks);

}