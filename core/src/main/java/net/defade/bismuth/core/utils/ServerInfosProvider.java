package net.defade.bismuth.core.utils;

import net.defade.bismuth.core.listeners.server.ServerPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;

public interface ServerInfosProvider {
    ServerPacketListener getPacketListenerFromProtocol(ConnectionProtocol protocol);
}
