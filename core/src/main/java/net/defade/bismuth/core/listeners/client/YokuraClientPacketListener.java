package net.defade.bismuth.core.listeners.client;

import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class YokuraClientPacketListener extends ClientPacketListener {
    private final String serverName;

    public YokuraClientPacketListener(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public final void writeClientInfos(BismuthByteBuf byteBuf) {
        byteBuf.writeUTF(serverName);
    }
}
