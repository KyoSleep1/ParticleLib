package dev.sleep.particlelib.common.network.packet;

import dev.sleep.particlelib.common.network.NetworkManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PacketEmitterPlaced extends AbstractPacket {

    @Override
    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        //WRITE RELEVANT DATA
        return buf;
    }

    @Override
    public ResourceLocation getPacketID() {
        return NetworkManager.EMITTER_PLACED_PACKET_ID;
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        //Read relevant data and pass as a parameter
        client.execute(() -> runOnThread(client, handler, responseSender));
    }

    private static void runOnThread(Minecraft client, ClientPacketListener handler, PacketSender responseSender) {
        //Handle spawning
    }
}
