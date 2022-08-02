package net.defade.bismuth.core.protocol.packets.login.server;

import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundClientProtocolPacket implements Packet<ServerLoginPacketListener> {
    private final ConnectionProtocol connectionProtocol;
    private final BismuthByteBuf clientInfos;

    public ServerboundClientProtocolPacket(ConnectionProtocol connectionProtocol, BismuthByteBuf clientInfos) {
        this.connectionProtocol = connectionProtocol;
        this.clientInfos = clientInfos;
    }

    public ServerboundClientProtocolPacket(BismuthByteBuf byteBuf) {
        this.connectionProtocol = ConnectionProtocol.values()[byteBuf.readByte()];
        this.clientInfos = new BismuthByteBuf(byteBuf.readBytes(byteBuf.readInt()));
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeByte(connectionProtocol.ordinal());
        out.writeInt(clientInfos.readableBytes());
        out.writeBytes(clientInfos.copy());
    }

    @Override
    public void handle(ServerLoginPacketListener packetListener) {
        packetListener.handleClientProtocol(this);
    }

    public ConnectionProtocol getConnectionProtocol() {
        return connectionProtocol;
    }

    public BismuthByteBuf getClientInfos() {
        return clientInfos;
    }
}
