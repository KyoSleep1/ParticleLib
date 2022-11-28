package dev.sleep.particlelib.impl.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractPacket {

    public abstract FriendlyByteBuf encode();

    public abstract ResourceLocation getPacketID();
}
