package dev.sleep.particlelib.common.network.packet;

import dev.sleep.particlelib.client.ParticleManager;
import dev.sleep.particlelib.common.network.NetworkManager;
import dev.sleep.particlelib.example.common.particle.SnowParticleEmitter;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PacketEmitterPlaced extends AbstractPacket {

    private final BlockPos BLOCK_POS;

    public PacketEmitterPlaced(BlockPos blockPos){
        this.BLOCK_POS = blockPos;
    }

    @Override
    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(BLOCK_POS);
        return buf;
    }

    @Override
    public ResourceLocation getPacketID() {
        return NetworkManager.EMITTER_PLACED_PACKET_ID;
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos blockPos = buf.readBlockPos();
        client.execute(() -> runOnThread(client, handler, responseSender, blockPos));
    }

    private static void runOnThread(Minecraft client, ClientPacketListener handler, PacketSender responseSender, BlockPos blockPos) {
        ParticleManager.INSTANCE.addToList(new SnowParticleEmitter(blockPos));
    }
}
