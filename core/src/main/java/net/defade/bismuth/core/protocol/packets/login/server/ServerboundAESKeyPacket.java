package net.defade.bismuth.core.protocol.packets.login.server;

import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundAESKeyPacket implements Packet<ServerLoginPacketListener> {
    private final byte[] encryptedAESKey;

    public ServerboundAESKeyPacket(byte[] encryptedAESKey) {
        this.encryptedAESKey = encryptedAESKey;
    }

    public ServerboundAESKeyPacket(BismuthByteBuf byteBuf) {
        this.encryptedAESKey = new byte[byteBuf.readInt()];
        byteBuf.readBytes(encryptedAESKey);
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeInt(encryptedAESKey.length);
        out.writeBytes(encryptedAESKey);
    }

    @Override
    public void handle(ServerLoginPacketListener packetListener) {
        packetListener.handleAESKey(this);
    }

    public byte[] getEncryptedAESKey() {
        return encryptedAESKey;
    }
}
