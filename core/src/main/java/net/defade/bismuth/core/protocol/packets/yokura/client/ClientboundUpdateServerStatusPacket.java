package net.defade.bismuth.core.protocol.packets.yokura.client;

import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.servers.ServerStatus;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundUpdateServerStatusPacket implements Packet<YokuraClientPacketListener> {
    private final Server server;
    private final ServerStatus serverStatus;

    public ClientboundUpdateServerStatusPacket(Server server, ServerStatus serverStatus) {
        this.server = server;
        this.serverStatus = serverStatus;
    }

    public ClientboundUpdateServerStatusPacket(BismuthByteBuf byteBuf) {
        this.server = byteBuf.readServer();
        this.serverStatus = ServerStatus.valueOf(byteBuf.readUTF());
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeServer(server);
        out.writeUTF(serverStatus.toString());
    }

    @Override
    public void handle(YokuraClientPacketListener packetListener) {
        packetListener.handleUpdateServerStatus(this);
    }

    public Server getServer() {
        return server;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }
}
