package dev.sleep.particlelib.example.common.particle;

import dev.sleep.particlecore.DefaultedParticleEmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class SnowParticleEmitter extends DefaultedParticleEmitter {

    public SnowParticleEmitter(BlockPos targetPos) {
        super(targetPos);
    }

    public SnowParticleEmitter(LivingEntity targetEntity) {
        super(targetEntity);
    }
}
