package dev.sleep.particlelib;

import dev.sleep.particlelib.core.impl.common.registry.ItemRegistry;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main implements ModInitializer {

    @Getter
    private static final boolean developmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();

    @Getter
    private static final Logger Logger = LoggerFactory.getLogger(Reference.MODID);

    @Override
    public void onInitialize() {
        new ItemRegistry();
    }

    public static void warnCritical(String message, IOException e) {
        Main.getLogger().warn(message);

        if (e != null) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
