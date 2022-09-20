package net.defade.bismuth.core.listeners.server;

import java.net.InetSocketAddress;
import net.defade.bismuth.core.protocol.packets.yokura.server.ServerboundUpdateServerStatusPacket;
import net.defade.bismuth.core.servers.GameType;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.ServerInfosProvider;

public abstract class YokuraServerPacketListener extends ServerPacketListener {
    private GameType gameType;
    private String velocityIdTracker = null;
    private InetSocketAddress address;
    private Server server;

    public void updateServerStatus(ServerboundUpdateServerStatusPacket updateServerStatusPacket) {
        server.setServerStatus(updateServerStatusPacket.getServerStatus());
    }

    @Override
    public final void readClientInfos(BismuthByteBuf clientInfos) {
        this.gameType = clientInfos.readGameType();
        if(clientInfos.readBoolean()) this.velocityIdTracker = clientInfos.readUTF();
        this.address = new InetSocketAddress(clientInfos.readUTF(), clientInfos.readInt());
    }

    @Override
    public final void writeServerInfos(ServerInfosProvider serverInfosProvider, BismuthByteBuf serverInfos) {
        this.server = serverInfosProvider.getServer(gameType, velocityIdTracker);
        serverInfos.writeServer(server);
        serverInfosProvider.getNetworkInfos().write(serverInfos);
    }

    public Server getServer() {
        return server;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}
