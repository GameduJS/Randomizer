package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.visuals.RandomizerScoreboard;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final PlatformLoader platformLoader;
    private final RandomizerScoreboard randomizerScoreboard;

    public PlayerListener(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
        this.randomizerScoreboard = gameDispatcher.getRandomizerScoreboard();
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(!Option.ENABLE_HUNGER.getValue().getAsBoolean());
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
        if(!Option.ENABLE_PVP.getValue().getAsBoolean())
            if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
                e.setCancelled(true);
    }

    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent e) {
        MessageHandler.registerLanguage(e.getPlayer());
    }
}
