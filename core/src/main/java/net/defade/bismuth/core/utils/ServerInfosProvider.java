package net.defade.bismuth.core.utils;

import net.defade.bismuth.core.listeners.server.ServerPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.servers.GameType;
import net.defade.bismuth.core.servers.Server;

public interface ServerInfosProvider {
    ServerPacketListener getPacketListenerFromProtocol(ConnectionProtocol protocol);

    NetworkInfos getNetworkInfos();

    Server getServer(GameType gameType, String velocityIdTracker);
}
