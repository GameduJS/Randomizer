package de.gamdude.randomizer.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.gamdude.randomizer.base.GameDispatcher;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
        if(canBreakBlock)
            return;
        if(placedBlocksHashList.contains(event.getBlock().getLocation().hashCode()) && event.getBlock().getType() != Material.BARRIER)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        placedBlocksHashList.add(event.getBlock().getLocation().hashCode());
        int zOff = (event.getBlock().getType().data == Bed.class) ? 1 : 0;
        Location placedBlockLocation = event.getBlock().getLocation().clone().add(0, 0, zOff);

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
