package net.defade.bismuth.core.listeners.client;

import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundCreatedServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundDeletedServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundForwardingKeyPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundServersListPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundStopServerPacket;
import net.defade.bismuth.core.protocol.packets.yokura.client.ClientboundUpdateServerStatusPacket;
import net.defade.bismuth.core.servers.GameType;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.NetworkInfos;

public abstract class YokuraClientPacketListener extends ClientPacketListener {
    private final GameType gameType;
    private final String velocityIdTracker;
    private final int port;
    private Server server;
    private NetworkInfos networkInfos;

    public YokuraClientPacketListener(GameType gameType, String velocityIdTracker, int port) {
        this.gameType = gameType;
        this.velocityIdTracker = velocityIdTracker;
        this.port = port;
    }

    public abstract void handleForwardingKey(ClientboundForwardingKeyPacket forwardingKeyPacket);

    public abstract void handleServersList(ClientboundServersListPacket serversListPacket);

    public abstract void handleCreatedServer(ClientboundCreatedServerPacket createdServerPacket);

    public abstract void handleDeletedServer(ClientboundDeletedServerPacket deletedServerPacket);

    public abstract void handleUpdateServerStatus(ClientboundUpdateServerStatusPacket updateServerStatusPacket);

    public abstract void handleStopServer(ClientboundStopServerPacket stopServerPacket);

    @Override
    public final void writeClientInfos(BismuthByteBuf byteBuf) {
        byteBuf.writeGameType(gameType);
        byteBuf.writeBoolean(velocityIdTracker != null);
        if(velocityIdTracker != null) byteBuf.writeUTF(velocityIdTracker);
        byteBuf.writeInt(port);
    }

    @Override
    public final void readServerInfos(BismuthByteBuf byteBuf) {
        this.server = byteBuf.readServer();
        this.networkInfos = new NetworkInfos(byteBuf);
    }

    public Server getServer() {
        return server;
    }

    public NetworkInfos getNetworkInfos() {
        return networkInfos;
    }
}
