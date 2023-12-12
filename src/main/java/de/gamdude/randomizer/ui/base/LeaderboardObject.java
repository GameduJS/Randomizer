package de.gamdude.randomizer.ui.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardObject {

    private final int playerPosition;
    private final List<UUID> playerList;

    public LeaderboardObject(int playerPosition) {
        this.playerPosition = playerPosition;
        this.playerList = new ArrayList<>();
    }

    public void addPlayer(UUID uuid) {
        this.playerList.add(uuid);
    }

    public List<UUID> getPlayerList() {
        return playerList;
    }
}
