package net.defade.bismuth.core.protocol.packets.login.client;

import net.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundServerInfosPacket implements Packet<ClientLoginPacketListener> {
    private final BismuthByteBuf serverInfos;

    public ClientboundServerInfosPacket(BismuthByteBuf byteBuf) {
        this.serverInfos = new BismuthByteBuf(byteBuf.copy());
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeBytes(serverInfos.copy());
    }

    @Override
    public void handle(ClientLoginPacketListener packetListener) {
        packetListener.handleServerInfos(this);
    }

    public BismuthByteBuf getServerInfos() {
        return serverInfos;
    }
}
