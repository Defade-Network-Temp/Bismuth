package net.defade.bismuth.core.protocol.packets.yokura.client;

import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundStopServerPacket implements Packet<YokuraClientPacketListener> {
    private final String reason;

    public ClientboundStopServerPacket(String reason) {
        this.reason = reason;
    }

    public ClientboundStopServerPacket(BismuthByteBuf byteBuf) {
        this.reason = byteBuf.readUTF();
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeUTF(reason);
    }

    @Override
    public void handle(YokuraClientPacketListener packetListener) {
        packetListener.handleStopServer(this);
    }

    public String getReason() {
        return reason;
    }
}
