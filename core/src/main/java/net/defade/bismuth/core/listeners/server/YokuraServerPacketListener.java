package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.protocol.packets.yokura.server.ServerboundUpdateServerStatusPacket;
import net.defade.bismuth.core.servers.Server;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.ServerInfosProvider;

public abstract class YokuraServerPacketListener extends ServerPacketListener {
    private String serverId;
    private Server server;

    public void updateServerStatus(ServerboundUpdateServerStatusPacket updateServerStatusPacket) {
        server.setServerStatus(updateServerStatusPacket.getServerStatus());
    }

    @Override
    public final void readClientInfos(BismuthByteBuf clientInfos) {
        this.serverId = clientInfos.readUTF();
    }

    @Override
    public final void writeServerInfos(ServerInfosProvider serverInfosProvider, BismuthByteBuf serverInfos) {
        this.server = serverInfosProvider.getServerFromServerId(serverId);
        serverInfos.writeServer(server);
    }

    public Server getServer() {
        return server;
    }
}
