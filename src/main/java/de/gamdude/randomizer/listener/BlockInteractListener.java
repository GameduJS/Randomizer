package de.gamdude.randomizer.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.gamdude.randomizer.base.GameDispatcher;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockInteractListener implements Listener {
    private final List<Integer> placedBlocksHashList = new ArrayList<>();

    private final GameDispatcher gameDispatcher;
    private final boolean canBreakBlock;

    public BlockInteractListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.canBreakBlock = gameDispatcher.getConfig().getProperty("canBreakBlock").getAsBoolean();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(gameDispatcher.getState() != 1 && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
            return;
        }
        if(canBreakBlock)
            return;
        if(placedBlocksHashList.contains(event.getBlock().getLocation().hashCode()) && event.getBlock().getType() != Material.BARRIER)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        if(gameDispatcher.getState() != 1 && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();

        placedBlocksHashList.add(block.getLocation().hashCode());
        int zOff = (block.getType().data == Bed.class) ? ((Bed) block.getBlockData()).getFacing().getModZ() : 0;
        Location placedBlockLocation = block.getLocation().clone().add(0, 0, zOff);

        if(gameDispatcher.getPlayerProgressHandle().placeBlock(event.getPlayer(), placedBlockLocation))
            gameDispatcher.getLeaderboardHandler().updateLeaderboard(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {
        if(placedBlocksHashList.contains(event.getBlock().getLocation().hashCode()))
            event.setCancelled(true);
    }

    // Water breaking skulls
    @EventHandler
    public void onBlockDestroy(BlockDestroyEvent e) {
        e.setWillDrop(false);
    }

}
