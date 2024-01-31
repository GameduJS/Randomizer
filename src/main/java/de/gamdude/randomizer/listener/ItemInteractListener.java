package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.ui.menu.ConfigMenu;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public record ItemInteractListener(GameDispatcher gameDispatcher) implements Listener {

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null)
            return;
        if(event.getAction().isLeftClick())
            return;
        if(!event.getItem().hasItemMeta())
            return;
        if(!event.getItem().getItemMeta().getPersistentDataContainer().has(ItemBuilder.ITEM_KEY, PersistentDataType.STRING))
            return;
        switch (Objects.requireNonNull(event.getItem().getItemMeta().getPersistentDataContainer().get(ItemBuilder.ITEM_KEY, PersistentDataType.STRING))) {
            case "settings-item":
                player.openInventory(new ConfigMenu(gameDispatcher).getInventory());
                break;
            case "start-item":
                player.getInventory().clear();
                gameDispatcher.startGame();
                break;
            default: throw new NotImplementedException("Not implemented!");
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!event.getItemDrop().getItemStack().hasItemMeta())
            return;
        String data = event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().get(ItemBuilder.ITEM_KEY, PersistentDataType.STRING);
        if(data == null)
            return;
        if(data.equals("settings-item") || data.equals("start-item"))
            event.setCancelled(true);
    }

}
