package net.defade.bismuth.core.utils;

import net.defade.bismuth.core.listeners.server.ServerPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.servers.Server;

public interface ServerInfosProvider {
    ServerPacketListener getPacketListenerFromProtocol(ConnectionProtocol protocol);

    Server getServerFromServerId(String serverId);
}
