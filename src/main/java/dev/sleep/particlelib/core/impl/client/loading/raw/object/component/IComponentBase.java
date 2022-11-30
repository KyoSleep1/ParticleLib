package dev.sleep.particlelib.core.impl.client.loading.raw.object.component;

public interface IComponentBase {
    default int getSortingIndex() {
        return 0;
    }
}
