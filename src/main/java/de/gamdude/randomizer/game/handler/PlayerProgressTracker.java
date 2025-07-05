package de.gamdude.randomizer.game.handler;

import de.gamdude.randomizer.world.Platform;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProgressTracker implements Handler {

    private final PlatformLoader platformLoader;

    public PlayerProgressTracker(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    public boolean placeBlock(Player player, Block block) {
        Platform platform = platformLoader.getPlatform(player.getUniqueId());
        int zOff = (block.getType().data == Bed.class) ? ((Bed) block.getBlockData()).getFacing().getModZ() : 0;
        Location location = block.getLocation().clone().add(0, 0, zOff);

        platform.addBlock(location);

        double dz = location.clone().subtract(platform.getLastBlockBuilt()).z();
        if ( dz < 1 )
            return false;
        platform.setLastBlockBuilt(location);
        platform.increaseBlocksBuilt();
        return true;
    }

    public int getBlocksBuilt(UUID playerID) {
        return platformLoader.getPlatform(playerID).getBlocksBuilt();
    }

}
