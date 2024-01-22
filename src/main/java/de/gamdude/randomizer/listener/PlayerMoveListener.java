package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {

    private final GameDispatcher gameDispatcher;
    private final PlatformLoader platformLoader;

    public PlayerMoveListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(event.getTo().getBlockY() <= 20) {
            player.teleport(platformLoader.getPlatform(player.getUniqueId()).getPlatformLocation());
            player.setVelocity(new Vector(0,0,0));
            player.setFallDistance(0f);
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> MessageHandler.sendMessage(onlinePlayer, "playerFell", player.getName()));
        }

        if(player.getGameMode() != GameMode.SURVIVAL)
            return;

        if(gameDispatcher.getState() == 0) {
            if(event.hasChangedBlock())
                event.setCancelled(true);
        }

    }

}
