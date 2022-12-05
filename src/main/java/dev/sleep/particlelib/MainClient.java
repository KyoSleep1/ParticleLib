package dev.sleep.particlelib;

import dev.sleep.particlelib.client.ParticleComponentRegistry;
import dev.sleep.particlelib.client.loading.LoadingCache;
import dev.sleep.particlelib.client.renderer.ParticleRendererManager;
import dev.sleep.particlelib.common.network.NetworkManager;
import dev.sleep.particlelib.example.client.renderer.particle.SnowParticleRenderer;
import dev.sleep.particlelib.example.common.particle.SnowParticleEmitter;
import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerNetwork();
        this.registerResources();

        if (!Main.isDevelopmentEnvironment()) {
            return;
        }

        this.registerExamples();
    }

    private void registerNetwork() {
        NetworkManager.registerClientReceiverPackets();
    }

    private void registerResources() {
        ParticleComponentRegistry.registerAll();
        LoadingCache.loadResourcesAndCache();
    }

    private void registerExamples() {
        this.registerRenderers();
    }

    private void registerRenderers() {
        ParticleRendererManager.INSTANCE.registerRenderer(SnowParticleEmitter.class, new SnowParticleRenderer());
    }
}
