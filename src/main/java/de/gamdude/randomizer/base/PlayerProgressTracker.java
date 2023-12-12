package de.gamdude.randomizer.base;

import de.gamdude.randomizer.base.structure.Platform;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerProgressTracker {

    private final PlatformLoader platformLoader;

    public PlayerProgressTracker(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getPlatformLoader();
    }

    public void placeBlock(Player player, Location location) {
        Platform platform = platformLoader.getPlatform(player.getUniqueId());

        double dz = location.clone().subtract(platform.getLastBlockBuilt()).z();
        if(dz>=1) {
            platform.setLastBlockBuilt(location);
            platform.increaseBlocksBuilt();
        }
    }

    public int getBlocksBuilt(Player player) {
        return platformLoader.getPlatform(player.getUniqueId()).getBlocksBuilt();
    }

}
