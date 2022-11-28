package dev.sleep.particlelib.core.impl.common.network;

import dev.sleep.particlelib.Reference;
import dev.sleep.particlelib.core.impl.common.network.packet.AbstractPacket;
import dev.sleep.particlelib.core.impl.common.network.packet.PacketEmitterPlaced;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class NetworkManager {

    public static final ResourceLocation EMITTER_PLACED_PACKET_ID = new ResourceLocation(Reference.MODID, "emitter_placed");

    public static void registerClientReceiverPackets() {
        ClientPlayNetworking.registerGlobalReceiver(EMITTER_PLACED_PACKET_ID, PacketEmitterPlaced::receive);
    }

    public static void sendWithCallback(AbstractPacket packet, IPacketCallback callback) {
        callback.onReadyToSend(packet);
    }

    public static void sendToTrackingEntityAndSelf(AbstractPacket packet, Entity entityToTrack) {
        for (ServerPlayer trackingPlayer : PlayerLookup.tracking(entityToTrack)) {
            ServerPlayNetworking.send(trackingPlayer, packet.getPacketID(), packet.encode());
        }

        if(entityToTrack instanceof ServerPlayer serverPlayer)
            ServerPlayNetworking.send(serverPlayer, packet.getPacketID(), packet.encode());
    }

    public static void sendToEntitiesTrackingChunk(AbstractPacket packet, ServerLevel level, BlockPos blockPos) {
        for (ServerPlayer trackingPlayer : PlayerLookup.tracking(level, blockPos)) {
            ServerPlayNetworking.send(trackingPlayer, packet.getPacketID(), packet.encode());
        }
    }

    public interface IPacketCallback {
        void onReadyToSend(AbstractPacket packetToSend);
    }
}
