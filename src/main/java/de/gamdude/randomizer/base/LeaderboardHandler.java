package de.gamdude.randomizer.base;

import de.gamdude.randomizer.ui.base.LeaderboardObject;
import de.gamdude.randomizer.world.PlatformLoader;

import java.util.*;

public class LeaderboardHandler {

    private final PlatformLoader platformLoader;
    private final Map<UUID, Integer> positionPlayerMap;

    public LeaderboardHandler(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getPlatformLoader();
        this.positionPlayerMap = new HashMap<>();
    }

    public void updateLeaderboard(UUID uuid) {
        positionPlayerMap.put(uuid, platformLoader.getPlatform(uuid).getBlocksBuilt());
    }

    public int getPosition(UUID uuid) {
        List<UUID> sortedPlayers = getSortedPlayer();
        int playerPosition = sortedPlayers.indexOf(uuid);

        if (playerPosition != -1) {
            UUID currentPlayer = sortedPlayers.get(playerPosition);

            int startRange = playerPosition;
            int endRange = playerPosition;

            while (startRange > 0 && positionPlayerMap.get(sortedPlayers.get(startRange - 1)).equals(positionPlayerMap.get(currentPlayer))) {
                startRange--;
            }

            while (endRange < sortedPlayers.size() - 1 && positionPlayerMap.get(sortedPlayers.get(endRange + 1)).equals(positionPlayerMap.get(currentPlayer))) {
                endRange++;
            }

            LeaderboardObject leaderboardObject = new LeaderboardObject(playerPosition);

            // Players before and after
            if (startRange > 0)
                leaderboardObject.addPlayer(sortedPlayers.get(startRange - 1));
            leaderboardObject.addPlayer(uuid);
            if (endRange < sortedPlayers.size() - 1)
                leaderboardObject.addPlayer(sortedPlayers.get(endRange + 1));

            return startRange + 1;
        }
        return -1;
    }

    private List<UUID> getSortedPlayer() {
        return positionPlayerMap.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public LeaderboardObject getTopPlayers() {
        LeaderboardObject leaderboardObject = new LeaderboardObject(1);
        List<UUID> sortedPlayers = getSortedPlayer();
        for(int i = 0; i < Math.min(5, sortedPlayers.size()); ++i) {
            leaderboardObject.addPlayer(sortedPlayers.get(i));
        }
        return leaderboardObject;
    }
}
