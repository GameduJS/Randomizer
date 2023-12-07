package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.GameDispatcher;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final GameDispatcher gameDispatcher;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public PlayerListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(miniMessage.deserialize("<gray>You just died!"));
        event.setCancelled(true);
        event.getPlayer().spigot().respawn();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(gameDispatcher.getPlatformLoader().getPlatform(event.getPlayer().getUniqueId()).getPlatformLocation());
    }
}
