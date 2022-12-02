package dev.sleep.particlelib.client.loading.object.type;

public enum ParticleMaterialType {
    OPAQUE("particles_opaque"), ALPHA("particles_alpha"), BLEND("particles_blend");

    public final String particleName;

    ParticleMaterialType(String particleName) {
        this.particleName = particleName;
    }

    public static ParticleMaterialType fromString(String material) {
        for (ParticleMaterialType materialType : values()) {
            if (materialType.particleName.equals(material)) {
                return materialType;
            }
        }

        return OPAQUE;
    }
}
