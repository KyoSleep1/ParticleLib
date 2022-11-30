package dev.sleep.particlelib.core.impl.client.loading.raw.object.type;

public enum ParticleCurveType {

    LINEAR("linear"), HERMITE("catmull_rom");

    public final String curveName;

    ParticleCurveType(String curveName) {
        this.curveName = curveName;
    }
}
