package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.servers.Game;
import net.defade.bismuth.core.servers.GameType;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.ServerInfosProvider;
import java.util.ArrayList;
import java.util.List;

public abstract class YokuraServerPacketListener extends ServerPacketListener {
    private String serverId;
    private GameType gameType;
    private final List<Game> games = new ArrayList<>();

    @Override
    public final void readClientInfos(BismuthByteBuf clientInfos) {
        this.gameType = clientInfos.readGameType();
        this.serverId = clientInfos.readUTF();
    }

    @Override
    public final void writeServerInfos(ServerInfosProvider serverInfosProvider, BismuthByteBuf serverInfos) {

    }

    public String getServerId() {
        return serverId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public List<Game> getGames() {
        return games;
    }
}
