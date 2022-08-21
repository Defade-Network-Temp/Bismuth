package net.defade.bismuth.core.protocol.packets.yokura.client;

import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundForwardingKeyPacket implements Packet<YokuraClientPacketListener> {
    private final byte[] forwardingKey;

    public ClientboundForwardingKeyPacket(byte[] forwardingKey) {
        this.forwardingKey = forwardingKey;
    }

    public ClientboundForwardingKeyPacket(BismuthByteBuf byteBuf) {
        this.forwardingKey = new byte[byteBuf.readInt()];
        byteBuf.readBytes(forwardingKey);
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeInt(forwardingKey.length);
        out.writeBytes(forwardingKey);
    }

    @Override
    public void handle(YokuraClientPacketListener packetListener) {
        packetListener.handleForwardingKey(this);
    }

    public byte[] getForwardingKey() {
        return forwardingKey;
    }
}
