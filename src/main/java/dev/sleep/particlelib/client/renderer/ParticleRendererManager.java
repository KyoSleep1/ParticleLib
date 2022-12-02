package dev.sleep.particlelib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.client.renderer.AbstractParticleRenderer;
import dev.sleep.particlelib.client.ParticleManager;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.HashMap;

public class ParticleRendererManager {

    public static final ParticleRendererManager INSTANCE = new ParticleRendererManager();
    private final HashMap<Class<? extends AbstractParticleEmitter>, AbstractParticleRenderer> PARTICLES_RENDERER_LIST = new HashMap<>();

    public void registerRenderer(Class<? extends AbstractParticleEmitter> particleClass, AbstractParticleRenderer particleRenderer) {
        PARTICLES_RENDERER_LIST.putIfAbsent(particleClass, particleRenderer);
    }

    public void renderParticles(PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        ParticleManager.INSTANCE.fetchList(particleEmitter -> {
            final AbstractParticleRenderer PARTICLE_RENDERER = PARTICLES_RENDERER_LIST.get(particleEmitter.getClass());
            if (PARTICLE_RENDERER == null) {
                return;
            }

            PARTICLE_RENDERER.render(particleEmitter, poseStack, buffer, lightTexture, activeRenderInfo, partialTicks);
            buffer.endBatch();
        });
    }
}
