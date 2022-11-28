package dev.sleep.particlelib;

import dev.sleep.particlelib.impl.common.registry.ItemRegistry;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

    @Getter
    private static final Logger Logger = LoggerFactory.getLogger(Reference.MODID);

    @Override
    public void onInitialize() {
        new ItemRegistry();
    }
}
