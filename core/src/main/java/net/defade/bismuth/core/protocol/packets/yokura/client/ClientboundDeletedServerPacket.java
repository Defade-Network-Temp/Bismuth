package net.defade.bismuth.core.protocol.packets.yokura.client;

import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundDeletedServerPacket implements Packet<YokuraClientPacketListener> {
    private final Server server;

    public ClientboundDeletedServerPacket(Server server) {
        this.server = server;
    }

    public ClientboundDeletedServerPacket(BismuthByteBuf byteBuf) {
        this.server = byteBuf.readServer();
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeServer(server);
    }

    @Override
    public void handle(YokuraClientPacketListener packetListener) {
        packetListener.handleDeletedServer(this);
    }

    public Server getServer() {
        return server;
    }
}
