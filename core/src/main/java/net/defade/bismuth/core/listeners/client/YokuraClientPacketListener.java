package net.defade.bismuth.core.listeners.client;

import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundCreatedServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundDeletedServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundForwardingKeyPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundServersListPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundStopServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundUpdateServerStatusPacket;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class YokuraClientPacketListener extends ClientPacketListener {
    private final String serverId;
    private Server server;

    public YokuraClientPacketListener(String serverId) {
        this.serverId = serverId;
    }

    public abstract void handleForwardingKey(ClientboundForwardingKeyPacket forwardingKeyPacket);

    public abstract void handleServersList(ClientboundServersListPacket serversListPacket);

    public abstract void handleCreatedServer(ClientboundCreatedServerPacket createdServerPacket);

    public abstract void handleDeletedServer(ClientboundDeletedServerPacket deletedServerPacket);

    public abstract void handleUpdateServerStatus(ClientboundUpdateServerStatusPacket updateServerStatusPacket);

    public abstract void handleStopServer(ClientboundStopServerPacket stopServerPacket);

    @Override
    public final void writeClientInfos(BismuthByteBuf byteBuf) {
        byteBuf.writeUTF(serverId);
    }

    @Override
    public final void readServerInfos(BismuthByteBuf byteBuf) {
        this.server = byteBuf.readServer();
    }

    public Server getServer() {
        return server;
    }
}
