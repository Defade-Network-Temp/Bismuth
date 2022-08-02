package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class YokuraServerPacketListener extends ServerPacketListener {
    private String serverName;

    @Override
    public final void readClientInfos(BismuthByteBuf clientInfos) {
        this.serverName = clientInfos.readUTF();
    }

    public String getServerName() {
        return serverName;
    }
}
