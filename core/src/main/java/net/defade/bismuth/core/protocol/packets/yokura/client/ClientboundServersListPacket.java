package net.defade.bismuth.core.protocol.packets.yokura.client;

import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;

import java.util.HashSet;
import java.util.Set;

public class ClientboundServersListPacket implements Packet<YokuraClientPacketListener> {
    private final Set<Server> servers;

    public ClientboundServersListPacket(Set<Server> servers) {
        this.servers = servers;
    }

    public ClientboundServersListPacket(BismuthByteBuf byteBuf) {
        servers = new HashSet<>();
        int serversAmount = byteBuf.readInt();
        for (int i = 0; i < serversAmount; i++) {
            servers.add(byteBuf.readServer());
        }
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeInt(servers.size());
        for (Server server : servers) {
            out.writeServer(server);
        }
    }

    @Override
    public void handle(YokuraClientPacketListener packetListener) {
        packetListener.handleServersList(this);
    }

    public Set<Server> getServers() {
        return servers;
    }
}
