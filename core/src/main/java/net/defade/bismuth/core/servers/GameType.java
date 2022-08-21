package net.defade.bismuth.core.servers;

import java.util.Objects;

public class GameType {
    private final String miniGame;
    private final String miniGameType;

    public GameType(String miniGame, String miniGameType) {
        this.miniGame = miniGame;
        this.miniGameType = miniGameType;
    }

    public String getMiniGame() {
        return miniGame;
    }

    public String getMiniGameType() {
        return miniGameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameType gameType1 = (GameType) o;
        return miniGame.equals(gameType1.miniGame) && miniGameType.equals(gameType1.miniGameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miniGame, miniGameType);
    }

    @Override
    public String toString() {
        return miniGame + "-" + miniGameType;
    }
}
