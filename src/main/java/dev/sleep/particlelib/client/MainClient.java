package dev.sleep.particlelib.client;

import dev.sleep.particlelib.Main;
import dev.sleep.particlelib.example.client.renderer.particle.SnowParticleRenderer;
import dev.sleep.particlelib.example.common.particle.SnowParticleEmitter;
import dev.sleep.particlelib.core.impl.client.renderer.ParticleRendererManager;
import dev.sleep.particlelib.core.impl.common.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerNetwork();
        if(!Main.isDevelopmentEnvironment()){
            return;
        }

        this.registerExamples();
    }

    private void registerNetwork(){
        NetworkManager.registerClientReceiverPackets();
    }

    private void registerExamples(){
        this.registerRenderers();
    }

    private void registerRenderers(){
        ParticleRendererManager.INSTANCE.registerRenderer(SnowParticleEmitter.class, new SnowParticleRenderer());
    }
}
