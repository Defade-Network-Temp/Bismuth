package net.defade.bismuth.core.listeners.client;

import net.defade.bismuth.core.servers.GameType;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public abstract class YokuraClientPacketListener extends ClientPacketListener {
    private final GameType gameType;
    private final String serverId;

    public YokuraClientPacketListener(GameType gameType, String serverId) {
        this.gameType = gameType;
        this.serverId = serverId;
    }

    @Override
    public final void writeClientInfos(BismuthByteBuf byteBuf) {
        byteBuf.writeGameType(gameType);
        byteBuf.writeUTF(serverId);
    }

    @Override
    public final void readServerInfos(BismuthByteBuf byteBuf) {

    }

    public GameType getGameType() {
        return gameType;
    }

    public String getServerId() {
        return serverId;
    }
}
