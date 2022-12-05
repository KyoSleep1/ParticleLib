package dev.sleep.particlecore;

import com.eliotlash.mclib.math.IValue;
import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.*;

public abstract class AbstractParticleEmitter {

    public final UUID particleUUID = UUID.randomUUID();

    public ClientLevel world;
    public LivingEntity targetEntity;
    public BlockPos targetPos;

    public double age, spawnedParticles, sanityTicks;

    public boolean playing = true, removed = false;
    public int lifetime;

    public Vector3d lastGlobal = new Vector3d();
    public Matrix3f rotation = new Matrix3f();

    public final Map<String, IValue> DATA_LIST = Collections.emptyMap();
    public final List<EnhancedParticle> PARTICLES_LIST = new ArrayList<>();

    public void start() {
        if (this.playing) {
            return;
        }

        this.age = 0;
        this.spawnedParticles = 0;
        this.playing = true;
    }

    public void spawnParticle(CachedParticleScheme scheme) {
        if (!this.playing) {
            return;
        }

        this.PARTICLES_LIST.add(this.createParticle(scheme));
    }

    private EnhancedParticle createParticle(CachedParticleScheme scheme) {
        EnhancedParticle particle = new EnhancedParticle();
        for (AbstractComponent component : scheme.getComponentsList()) {
            component.apply(this, particle, false);
        }

        if (particle.useRelativePosition && !particle.useRelativeRotation) {
            Vector3f vec = new Vector3f(particle.position);

            particle.matrix.transform(vec);
            particle.position.x = vec.x;
            particle.position.y = vec.y;
            particle.position.z = vec.z;
        }

        if (!(particle.useRelativePosition && particle.useRelativeRotation)) {
            particle.position.add((Vector3fc) this.lastGlobal);
            particle.initialPosition.add((Vector3fc) this.lastGlobal);
        }

        particle.prevPosition.set(particle.position);
        particle.rotation = particle.initialRotation;
        particle.prevRotation = particle.rotation;

        return particle;
    }

    public void stop() {
        if (!this.playing) {
            return;
        }

        this.spawnedParticles = 0;
        this.playing = false;
        this.removed = true;
    }

    public double getAge() {
        return this.getAge(0);
    }

    public double getAge(float partialTicks) {
        return (this.age + partialTicks) / 20.0;
    }

    public abstract void tick(CachedParticleScheme particleScheme);

    public abstract double getDistanceSq(double cameraX, double cameraY, double cameraZ);

    public abstract double getDistanceSq(EnhancedParticle particle, double cameraX, double cameraY, double cameraZ);
}
