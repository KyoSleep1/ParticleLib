package dev.sleep.particlecore;

import dev.sleep.particlecore.client.component.AbstractComponent;
import dev.sleep.particlelib.client.loading.object.CachedParticleScheme;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EnhancedParticle {

    public Matrix3f matrix = new Matrix3f();
    public boolean matrixSet;

    public Vector3f prevPosition = new Vector3f(), initialPosition = new Vector3f(), position = new Vector3f(),
            globalPosition = new Vector3f(), speed = new Vector3f();
    public float initialRotation, prevRotation, rotation, rotationVelocity, rotationAcceleration;

    public Quaternionf color = new Quaternionf();
    public boolean useRelativePosition = false, useRelativeRotation = false, dead = false;

    public Vector3f accelerationFactor = new Vector3f(1, 1, 1), acceleration = new Vector3f();
    public float drag, dragRotation, dragFactor;

    public int age, lifetime;
    public boolean manual, firstTick = true;

    public void update(AbstractParticleEmitter emitter, CachedParticleScheme scheme) {
        this.poseAndRotate();
        this.age();
        this.updateComponents(emitter, scheme);
    }

    public void poseAndRotate() {
        this.prevRotation = this.rotation;
        this.prevPosition.set(this.position);

        if (!this.manual) {
            float rotationAcceleration = this.rotationAcceleration / 20F - this.dragRotation * this.rotationVelocity;
            this.rotationVelocity += rotationAcceleration / 20F;
            this.rotation = this.initialRotation + this.rotationVelocity * this.age;

            Vector3f vec = new Vector3f(this.speed);
            vec.mul(-(this.drag + this.dragFactor));

            this.acceleration.add(vec);
            this.acceleration.mul(1 / 20F);
            this.speed.add(this.acceleration);

            vec.set(this.speed);
            vec.x *= this.accelerationFactor.x;
            vec.y *= this.accelerationFactor.y;
            vec.z *= this.accelerationFactor.z;

            if (this.useRelativePosition || this.useRelativeRotation) {
                this.matrix.transform(vec);
            }

            this.position.x += vec.x / 20F;
            this.position.y += vec.y / 20F;
            this.position.z += vec.z / 20F;
        }
    }

    public void age() {
        if (this.lifetime >= 0 && this.age >= this.lifetime) {
            this.dead = true;
        }

        if (firstTick) {
            firstTick = false;
        }

        this.age++;
    }

    private void updateComponents(AbstractParticleEmitter emitter, CachedParticleScheme scheme) {
        //TODO: Need to define particle molang variables
        for (AbstractComponent component : scheme.getComponentsList()) {
            component.update(emitter, null, scheme, false);
        }
    }

    public double getAge(float partialTick) {
        return (this.age + partialTick) / 20.0;
    }

    public Vector3f getGlobalPosition(AbstractParticleEmitter emitter) {
        return this.getGlobalPosition(emitter, this.position);
    }

    public Vector3f getGlobalPosition(AbstractParticleEmitter emitter, Vector3f vector) {
        double px = vector.x, py = vector.y, pz = vector.z;

        if (this.useRelativePosition && this.useRelativeRotation) {
            Vector3f v = new Vector3f((float) px, (float) py, (float) pz);
            emitter.rotation.transform(v);

            px = v.x;
            py = v.y;
            pz = v.z;

            px += emitter.lastGlobal.x;
            py += emitter.lastGlobal.y;
            pz += emitter.lastGlobal.z;
        }

        this.globalPosition.set(px, py, pz);
        return this.globalPosition;
    }
}
