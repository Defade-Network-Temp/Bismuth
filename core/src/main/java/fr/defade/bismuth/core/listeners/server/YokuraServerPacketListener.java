package fr.defade.bismuth.core.listeners.server;

import fr.defade.bismuth.core.protocol.packets.yokura.server.ServerboundServerNamePacket;

public abstract class YokuraServerPacketListener extends ServerPacketListener {
    public abstract void handleServerName(ServerboundServerNamePacket serverNamePacket);
}
