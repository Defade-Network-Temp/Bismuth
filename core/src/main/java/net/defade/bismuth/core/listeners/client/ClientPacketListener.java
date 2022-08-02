package net.defade.bismuth.core.listeners.client;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class ClientPacketListener extends PacketListener {
    public void writeClientInfos(BismuthByteBuf byteBuf) { }
}
