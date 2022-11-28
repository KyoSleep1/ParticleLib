package dev.sleep.particlelib.impl.client;

import dev.sleep.particlelib.impl.common.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerNetwork();
    }

    private void registerNetwork(){
        NetworkManager.registerClientReceiverPackets();
    }
}
