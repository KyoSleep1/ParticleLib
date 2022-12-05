package dev.sleep.particlecore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlecore.client.renderer.scheme.AbstractParticleScheme;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import lombok.Getter;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractEmitterRenderer {

    @Getter
    protected final AbstractParticleScheme ParticleScheme;

    public AbstractEmitterRenderer(AbstractParticleScheme particleScheme) {
        this.ParticleScheme = particleScheme;
    }

    public abstract void preRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks);

    public abstract void render(AbstractParticleEmitter particleEmitter, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks);

    public abstract void postRender(AbstractParticleEmitter particleEmitter, CachedParticleScheme scheme, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LightTexture lightTexture, Camera activeRenderInfo, float partialTicks);

    public void setupParticleMatrix(AbstractParticleEmitter emitter, EnhancedParticle particle) {
        if (particle.useRelativePosition) {
            if (particle.useRelativeRotation) {
                particle.matrix.identity();
                return;
            }

            if (!particle.matrixSet) {
                particle.matrix.set(emitter.rotation);
                particle.matrixSet = true;
                return;
            }
            return;
        }

        if (particle.useRelativeRotation) {
            particle.matrix.set(emitter.rotation);
        }
    }

    public void setupCameraPropertiesAndSort(AbstractParticleEmitter emitter, Camera activeInfo) {
        Vec3 cameraPosition = activeInfo.getPosition();
        emitter.PARTICLES_LIST.sort((particleA, particleB) -> {
            double particleADistance = emitter.getDistanceSq(particleA, cameraPosition.x, cameraPosition.y, cameraPosition.z);
            double particleBDistance = emitter.getDistanceSq(particleB, cameraPosition.x, cameraPosition.y, cameraPosition.z);

            if (particleADistance < particleBDistance) {
                return 1;
            } else if (particleADistance > particleBDistance) {
                return -1;
            }

            return 0;
        });
    }
}
