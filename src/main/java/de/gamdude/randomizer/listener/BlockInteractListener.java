package de.gamdude.randomizer.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.game.handler.LeaderboardHandler;
import de.gamdude.randomizer.game.handler.PlayerProgressTracker;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.world.Platform;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockInteractListener implements Listener {

    private final GameDispatcher gameDispatcher;
    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;
    private final PlatformLoader platformLoader;

    public BlockInteractListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        // Creative players are able to break any block
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;
        // Players are only allow to break blocks during ingame state
        if (gameDispatcher.getState() != 1) {
            event.setCancelled(true);
            return;
        }
        // If enabled 'canBreakBlock' players can break any block
        if (Option.ENABLE_BREAK_BLOCK.getValue().getAsBoolean())
            return;

        // If block was placed & players cannot break blocks, disallow it.
        if(isPlacedBlock(event.getBlock()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onTreeGrowth(StructureGrowEvent event) {
        Location location = event.getLocation();
        platformLoader.getPlatforms().stream().map(Map.Entry::getValue).filter(platform -> platform.locationWithIn(event.getLocation()))
                .forEach(platform -> platform.getBlocksList().remove(location)); // "forEach" only one platform
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        // Prevent building on top of the start position
        if(event.getBlockPlaced().getX() % 16 == 7 && event.getBlockPlaced().getZ() == 0) {
            event.setCancelled(true);
            return;
        }

        // disable placing as player when out of playing state
        if (gameDispatcher.getState() != 1 && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();

        //PlatformLoader platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
        //Location lastBlockBuilt = platformLoader.getPlatform(player.getUniqueId()).getLastBlockBuilt();
        //Vector vec = block.getLocation().toVector().subtract(lastBlockBuilt.toVector()).normalize();
        // vec can be (+-1, 0, 0) ; (0, +-1, 0) ; (0, 0, +-1)
        // only account for positiv z axis (map given) and/or positiv y <== based on goal

        if (playerProgressTracker.placeBlock(event.getPlayer(), block))
            leaderboardHandler.updateLeaderboard(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {
        if (isPlacedBlock(event.getBlock()) && !Option.BLOCK_DROP.getValue().getAsBoolean()) {
            getPlacedBlocks().remove(event.getBlock().getLocation());
            event.setCancelled(true);
        }
    }

    // Water breaking skulls
    @EventHandler
    public void onBlockDestroy(BlockDestroyEvent e) {
        e.setWillDrop(Option.BLOCK_DROP.getValue().getAsBoolean());
    }

    private boolean isPlacedBlock(Block block) {
        return getPlacedBlocks().contains(block.getLocation()) || (block.getBlockData() instanceof Bed bed && getPlacedBlocks().contains(block.getLocation().clone().subtract(bed.getFacing().getModX(), 0, bed.getFacing().getModZ())));
    }

    private List<Location> getPlacedBlocks() {
        return platformLoader.getPlatforms().stream().map(Map.Entry::getValue).map(Platform::getBlocksList).flatMap(List::stream).collect(Collectors.toList());
    }

}
