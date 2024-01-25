package de.gamdude.randomizer.base;

import de.gamdude.randomizer.world.Platform;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProgressTracker implements Handler {

    private final PlatformLoader platformLoader;

    public PlayerProgressTracker(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    public boolean placeBlock(Player player, Location location) {
        Platform platform = platformLoader.getPlatform(player.getUniqueId());

        double dz = location.clone().subtract(platform.getLastBlockBuilt()).z();
        if(dz >= 1) {
            platform.setLastBlockBuilt(location);
            platform.increaseBlocksBuilt();
            return true;
        }
        return false;
    }

    public int getBlocksBuilt(UUID playerID) {
        return platformLoader.getPlatform(playerID).getBlocksBuilt();
    }

}
