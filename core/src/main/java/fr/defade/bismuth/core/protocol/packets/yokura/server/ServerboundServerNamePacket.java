package fr.defade.bismuth.core.protocol.packets.yokura.server;

import fr.defade.bismuth.core.listeners.server.YokuraServerPacketListener;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.BismuthByteBuf;

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
