package dev.sleep.particlecore.client.component.expiration;

import dev.sleep.particlecore.AbstractParticleEmitter;
import dev.sleep.particlecore.EnhancedParticle;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.world.level.block.Block;

public class ComponentExpireNotInBlocks extends ComponentExpireBlocks {

    @Override
    public void update(AbstractParticleEmitter emitter, EnhancedParticle particle, CachedParticleScheme scheme, boolean updateEmitterOnly) {
        if(updateEmitterOnly){
            return;
        }

        if (particle.dead || emitter.world == null) {
            return;
        }

        Block current = this.getBlock(emitter, particle);
        for (Block block : this.blocks) {
            if (block == current) {
                return;
            }
        }

        particle.dead = true;
    }
}
