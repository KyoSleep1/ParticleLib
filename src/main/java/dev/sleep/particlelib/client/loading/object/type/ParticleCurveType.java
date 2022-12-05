package dev.sleep.particlelib.client.loading.object.type;

public enum ParticleCurveType {

    LINEAR("linear"), HERMITE("catmull_rom");

    public final String curveName;

    ParticleCurveType(String curveName) {
        this.curveName = curveName;
    }

    public static ParticleCurveType fromString(String curveName) {
        for (ParticleCurveType curveType : values()) {
            if (curveType.curveName.equals(curveName)) {
                return curveType;
            }
        }

        return LINEAR;
    }
}
