package dev.sleep.particlelib.core.impl.client.loading.raw.object.type;

public enum ParticleMaterialType {
    OPAQUE("particles_opaque"), ALPHA("particles_alpha"), BLEND("particles_blend");

    public final String particleName;

    ParticleMaterialType(String particleName) {
        this.particleName = particleName;
    }
}
