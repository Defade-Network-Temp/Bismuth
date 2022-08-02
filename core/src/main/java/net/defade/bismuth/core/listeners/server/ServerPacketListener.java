package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class ServerPacketListener extends PacketListener {
    public void readClientInfos(BismuthByteBuf clientInfos) { }
}
