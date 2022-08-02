package net.defade.bismuth.core.protocol.packets.yokura.server;

import net.defade.bismuth.core.listeners.server.YokuraServerPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundServerNamePacket implements Packet<YokuraServerPacketListener> {
    private final String name;

    public ServerboundServerNamePacket(String name) {
        this.name = name;
    }

    public ServerboundServerNamePacket(BismuthByteBuf byteBuf) {
        name = byteBuf.readUTF();
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeUTF(name);
    }

    @Override
    public void handle(YokuraServerPacketListener packetListener) {
        packetListener.handleServerName(this);
    }

    public String getName() {
        return name;
    }
}
