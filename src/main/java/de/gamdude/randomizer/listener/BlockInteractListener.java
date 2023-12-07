package de.gamdude.randomizer.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import de.gamdude.randomizer.base.GameDispatcher;
import org.bukkit.block.data.Directional;
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
        if(canBreakBlock)
            return;
        if(placedBlocksHashList.contains(event.getBlock().getLocation().hashCode()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        placedBlocksHashList.add(event.getBlock().getLocation().hashCode());
        int zOff = (event.getBlock().getBlockData() instanceof Directional directional) ? directional.getFacing().getModZ() : 0;

        gameDispatcher.getPlayerProgressHandle().placeBlock(event.getPlayer(), event.getBlock().getLocation().add(0, 0, zOff));
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
