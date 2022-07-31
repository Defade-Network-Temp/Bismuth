package fr.defade.bismuth.core.listeners.client;

import fr.defade.bismuth.core.protocol.packets.yokura.server.ServerboundServerNamePacket;

public class YokuraClientPacketListener extends ClientPacketListener {
    private final String serverName;

    public YokuraClientPacketListener(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public void channelActive() {
        sendPacket(new ServerboundServerNamePacket(serverName));
    }
}
