package dev.sleep.particlecore;

import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;

import java.util.Iterator;

public class DefaultedParticleEmitter extends AbstractParticleEmitter {

    public DefaultedParticleEmitter(BlockPos targetPos){
        this.targetPos = targetPos;
    }

    public DefaultedParticleEmitter(LivingEntity targetEntity){
        this.targetEntity = targetEntity;
    }

    @Override
    public void tick(CachedParticleScheme particleScheme) {
        if (!playing) {
            return;
        }

        this.followTarget(targetEntity != null);
        this.updateParticlesAndComponents(particleScheme);
        this.age();

        this.sanityTicks = 0;
        this.firstTick = false;
    }

    private void followTarget(boolean isEntity) {
        if (isEntity) {
            this.lastGlobal.set((Vector3dc) this.targetEntity.position());
            return;
        }

        this.lastGlobal.set(this.blockPosToVec(this.targetPos));
    }

    private Vector3d blockPosToVec(BlockPos blockPos) {
        return new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    private void updateParticlesAndComponents(CachedParticleScheme scheme) {
        if(this.firstTick){
            for(AbstractComponent component : scheme.getComponentsList()){
                component.apply(this, null, true);
            }
        }

        for (AbstractComponent component : scheme.getComponentsList()) {
            component.update(this, null, scheme, true);
        }

        Iterator<EnhancedParticle> particleIterator = PARTICLES_LIST.iterator();
        while (particleIterator.hasNext()) {
            EnhancedParticle particle = particleIterator.next();

            if (particle.dead) {
                particleIterator.remove();
                continue;
            }

            particle.update(this, scheme);
        }
    }

    private void age() {
        this.age++;
        this.sanityTicks += 1;
        this.playing = !removed;
    }

    @Override
    public double getDistanceSq(double cameraX, double cameraY, double cameraZ) {
        double dx = cameraX - this.lastGlobal.x;
        double dy = cameraY - this.lastGlobal.y;
        double dz = cameraZ - this.lastGlobal.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double getDistanceSq(EnhancedParticle particle, double cameraX, double cameraY, double cameraZ) {
        Vector3f pos = particle.getGlobalPosition(this);
        double dx = cameraX - pos.x;
        double dy = cameraY - pos.y;
        double dz = cameraZ - pos.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
