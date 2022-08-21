package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.ServerInfosProvider;

public abstract class ServerPacketListener extends PacketListener {
    public void readClientInfos(BismuthByteBuf clientInfos) { }

    public void writeServerInfos(ServerInfosProvider serverInfosProvider, BismuthByteBuf serverInfos) { }
}
