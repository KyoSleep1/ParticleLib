package dev.sleep.particlecore.client.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.sleep.particlelib.client.loading.object.component.AbstractComponent;
import lombok.Getter;

public class ComponentRegistry {

    @Getter
    private final static BiMap<String, Class<? extends AbstractComponent>> ComponentsList = HashBiMap.create();

     public static void registerAll() {

    }
}
