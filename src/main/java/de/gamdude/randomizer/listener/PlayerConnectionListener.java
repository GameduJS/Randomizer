package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.structure.Platform;
import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PlatformLoader platformLoader;
    private final GameDispatcher gameDispatcher;

    public PlayerConnectionListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.platformLoader = gameDispatcher.getPlatformLoader();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getLogger().info(event.getPlayer().getName() + " has joined the server!");

        Platform platform = platformLoader.createPlatform(player, Bukkit.getOnlinePlayers().size());
        platform.setEnabled(true);
        player.teleport(platform.getPlatformLocation());

        player.setScoreboard(gameDispatcher.getRandomizerScoreboard().getScoreboard());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(gameDispatcher.getState() == 1)
            platformLoader.getPlatform(player.getUniqueId()).setEnabled(false);
    }

}
