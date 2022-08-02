package net.defade.bismuth.core.protocol.packets.login.server;

import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundClientProtocolPacket implements Packet<ServerLoginPacketListener> {
    private final ConnectionProtocol connectionProtocol;

    public ServerboundClientProtocolPacket(ConnectionProtocol connectionProtocol) {
        this.connectionProtocol = connectionProtocol;
    }

    public ServerboundClientProtocolPacket(BismuthByteBuf byteBuf) {
        this.connectionProtocol = ConnectionProtocol.values()[byteBuf.readByte()];
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeByte(connectionProtocol.ordinal());
    }

    @Override
    public void handle(ServerLoginPacketListener packetListener) {
        packetListener.handleClientProtocol(this);
    }

    public ConnectionProtocol getConnectionProtocol() {
        return connectionProtocol;
    }
}
