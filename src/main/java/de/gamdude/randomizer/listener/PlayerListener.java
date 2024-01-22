package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final GameDispatcher gameDispatcher;
    private final PlatformLoader platformLoader;
    private final Config config;

    public PlayerListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
        this.config = gameDispatcher.getConfig();
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(!config.getProperty("canGetHungry").getAsBoolean());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        event.getPlayer().spigot().respawn();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        MessageHandler.sendMessage(event.getPlayer(), "playerDeath");
        event.setRespawnLocation(platformLoader.getPlatform(event.getPlayer().getUniqueId()).getPlatformLocation());
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        boolean enabled = config.getProperty("allowPVP").getAsBoolean();

        if(!enabled)
            if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
                e.setCancelled(true);
    }
}
