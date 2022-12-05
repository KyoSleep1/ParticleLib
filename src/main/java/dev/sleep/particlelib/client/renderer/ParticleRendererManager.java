package dev.sleep.particlelib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.client.renderer.AbstractEmitterRenderer;
import dev.sleep.particlelib.client.ParticleManager;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class ParticleRendererManager {

    public static final ParticleRendererManager INSTANCE = new ParticleRendererManager();
    public final HashMap<Class<? extends AbstractParticleEmitter>, AbstractEmitterRenderer> PARTICLES_RENDERER_LIST = new HashMap<>();

    public void registerRenderer(Class<? extends AbstractParticleEmitter> particleClass, AbstractEmitterRenderer particleRenderer) {
        PARTICLES_RENDERER_LIST.putIfAbsent(particleClass, particleRenderer);
    }

    public void renderParticles(PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks) {
        this.setupCameraPropertiesAndSort(activeRenderInfo);
        for (AbstractParticleEmitter emitter : ParticleManager.INSTANCE.PARTICLE_EMITTERS_LIST) {
            final AbstractEmitterRenderer PARTICLE_RENDERER = PARTICLES_RENDERER_LIST.get(emitter.getClass());
            if (PARTICLE_RENDERER == null) {
                return;
            }

            PARTICLE_RENDERER.render(emitter, poseStack, buffer, lightTexture, activeRenderInfo, partialTicks);
        }
    }

    private void setupCameraPropertiesAndSort(Camera activeInfo) {
        Vec3 cameraPosition = activeInfo.getPosition();
        ParticleManager.INSTANCE.PARTICLE_EMITTERS_LIST.sort((particleA, particleB) -> {
            double particleADistance = particleA.getDistanceSq(cameraPosition.x, cameraPosition.y, cameraPosition.z);
            double particleBDistance = particleB.getDistanceSq(cameraPosition.x, cameraPosition.y, cameraPosition.z);

            if (particleADistance < particleBDistance) {
                return 1;
            } else if (particleADistance > particleBDistance) {
                return -1;
            }

            return 0;
        });
    }

    public CachedParticleScheme getCachedSchemeByEmitterClass(Class<? extends AbstractParticleEmitter> clazz) {
        return PARTICLES_RENDERER_LIST.get(clazz).getParticleScheme().getCachedScheme();
    }
}
