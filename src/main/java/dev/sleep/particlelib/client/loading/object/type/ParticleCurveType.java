package dev.sleep.particlelib.client.loading.object.type;

public enum ParticleCurveType {

    LINEAR("linear"), HERMITE("catmull_rom");

    public final String curveName;

    ParticleCurveType(String curveName) {
        this.curveName = curveName;
    }
}
