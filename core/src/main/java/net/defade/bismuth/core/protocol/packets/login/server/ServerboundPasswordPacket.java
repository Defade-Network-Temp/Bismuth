package net.defade.bismuth.core.protocol.packets.login.server;

import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundPasswordPacket implements Packet<ServerLoginPacketListener> {
    private final byte[] password;

    public ServerboundPasswordPacket(byte[] password) {
        this.password = password;
    }

    public ServerboundPasswordPacket(BismuthByteBuf byteBuf) {
        this.password = new byte[byteBuf.readInt()];
        byteBuf.readBytes(password);
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeInt(password.length);
        out.writeBytes(password);
    }

    @Override
    public void handle(ServerLoginPacketListener packetListener) {
        packetListener.handlePassword(this);
    }

    public byte[] getPassword() {
        return password;
    }
}