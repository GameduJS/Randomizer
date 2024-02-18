package de.gamdude.randomizer.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.game.handler.LeaderboardHandler;
import de.gamdude.randomizer.game.handler.PlayerProgressTracker;
import de.gamdude.randomizer.game.options.Option;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockInteractListener implements Listener {
    private final List<Location> placedBlocksList = new ArrayList<>();

    private final GameDispatcher gameDispatcher;
    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;

    public BlockInteractListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
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
        placedBlocksList.remove(event.getLocation());
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

        placedBlocksList.add(block.getLocation());
        int zOff = (block.getType().data == Bed.class) ? ((Bed) block.getBlockData()).getFacing().getModZ() : 0;
        Location placedBlockLocation = block.getLocation().clone().add(0, 0, zOff);

        if (playerProgressTracker.placeBlock(event.getPlayer(), placedBlockLocation))
            leaderboardHandler.updateLeaderboard(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {
        if (isPlacedBlock(event.getBlock()) && !Option.BLOCK_DROP.getValue().getAsBoolean()) {
            placedBlocksList.remove(event.getBlock().getLocation());
            event.setCancelled(true);
        }
    }

    // Water breaking skulls
    @EventHandler
    public void onBlockDestroy(BlockDestroyEvent e) {
        e.setWillDrop(Option.BLOCK_DROP.getValue().getAsBoolean());
    }

    private boolean isPlacedBlock(Block block) {
        return placedBlocksList.contains(block.getLocation()) || (block.getBlockData() instanceof Bed bed && placedBlocksList.contains(block.getLocation().clone().subtract(bed.getFacing().getModX(), 0, bed.getFacing().getModZ())));
    }

}
