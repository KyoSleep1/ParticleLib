package dev.sleep.particlecore.client.component.shape;

import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import net.minecraft.world.entity.LivingEntity;

public class ComponentShapeEntityAABB extends ComponentShape {

    @Override
    public void apply(AbstractParticleEmitter emitter, EnhancedParticle particle, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        float centerX = (float) this.offset[0].get();
        float centerY = (float) this.offset[1].get();
        float centerZ = (float) this.offset[2].get();

        float w = 0;
        float h = 0;
        float d = 0;

        LivingEntity livingEntity = emitter.targetEntity;
        if(livingEntity != null){
            w = livingEntity.getBbWidth();
            h = livingEntity.getBbHeight();
            d = livingEntity.getBbWidth();
        }

        particle.position.x = centerX + ((float) Math.random() - 0.5F) * w;
        particle.position.y = centerY + ((float) Math.random() - 0.5F) * h;
        particle.position.z = centerZ + ((float) Math.random() - 0.5F) * d;

        if (this.surface) {
            int roll = (int) (Math.random() * 6 * 100) % 6;

            if (roll == 0) particle.position.x = centerX + w / 2F;
            else if (roll == 1) particle.position.x = centerX - w / 2F;
            else if (roll == 2) particle.position.y = centerY + h / 2F;
            else if (roll == 3) particle.position.y = centerY - h / 2F;
            else if (roll == 4) particle.position.z = centerZ + d / 2F;
            else if (roll == 5) particle.position.z = centerZ - d / 2F;
        }

        this.direction.applyDirection(particle, centerX, centerY, centerZ);
    }
}
