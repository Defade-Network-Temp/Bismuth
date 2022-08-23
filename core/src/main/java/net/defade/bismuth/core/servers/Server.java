package net.defade.bismuth.core.servers;

import java.util.Objects;

public class Server {
    private final String serverId;
    private final GameType gameType;
    private ServerStatus serverStatus;

    public Server(String serverId, GameType gameType, ServerStatus serverStatus) {
        this.serverId = serverId;
        this.gameType = gameType;
        this.serverStatus = serverStatus;
    }

    public String getServerId() {
        return serverId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return serverId.equals(server.serverId) && gameType.equals(server.gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, gameType);
    }
}
