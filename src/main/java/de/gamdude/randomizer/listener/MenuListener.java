package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.Randomizer;
import de.gamdude.randomizer.ui.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;

public record MenuListener(Randomizer plugin) implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onClick(InventoryClickEvent event) {
    	final InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Menu)) return;
        if(event.getCurrentItem() == null) return;
        if(event.getClickedInventory() == null) return;
        if(event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
        	event.setCancelled(true);
        	return;
        }
        event.setCancelled(((Menu) holder).onClick((Player)event.getWhoClicked(), event.getRawSlot(), event.getClick()));
    }

    @EventHandler 
    private void onOpen(InventoryOpenEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) ((Menu) holder).onOpen((Player)event.getPlayer());
    }

    @EventHandler 
    private void onClose(InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu)
            Bukkit.getScheduler().runTaskLater(plugin, () -> menu.onClose((Player) event.getPlayer()), 1);
    }
}