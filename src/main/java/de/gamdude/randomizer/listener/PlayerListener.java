package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.Randomizer;
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
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final GameDispatcher gameDispatcher;
    private final PlatformLoader platformLoader;
    private final RandomizerScoreboard randomizerScoreboard;

    public PlayerListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.randomizerScoreboard = gameDispatcher.getRandomizerScoreboard();
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(!Option.ENABLE_HUNGER.getValue().getAsBoolean());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!Option.KEEP_INVENTORY.getValue().getAsBoolean())
            event.getPlayer().getInventory().clear();
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
        if(!(e.getDamager() instanceof Player))
            return;
        if(!(e.getEntity() instanceof Player))
            return;
        if(Option.ENABLE_PVP.getValue().getAsBoolean())
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent e) {
        MessageHandler.registerLanguage(e.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                   if(gameDispatcher.getState() == 0)
                       randomizerScoreboard.setScoreboard(e.getPlayer());
                   else
                       randomizerScoreboard.updateScoreboard();
                }
            }.runTaskLater(Randomizer.getPlugin(Randomizer.class), 1);
    }
}
