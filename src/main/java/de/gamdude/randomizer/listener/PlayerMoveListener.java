package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

        if(player.getGameMode() != GameMode.SURVIVAL)
            return;

        if(event.getTo().getBlockY() <= 20) {
            player.teleport(platformLoader.getPlatform(player.getUniqueId()).getPlatformLocation());
            player.setVelocity(new Vector(0,0,0));
            player.setFallDistance(0f);
            if(gameDispatcher.getState() == 1)
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> MessageHandler.sendMessage(onlinePlayer, "playerFell", player.getName()));
        }

        if(gameDispatcher.getState() == 0 || gameDispatcher.getState() == 2) {
            if(event.hasChangedBlock()) {
                event.setCancelled(true);
                return;
            }
        }

        if(gameDispatcher.getState() > 0)
            if(hasChangedChunk(event.getTo(), event.getFrom()))
                event.setCancelled(true);

    }

    private boolean hasChangedChunk(Location to, Location from) {
        int x1 = to.getBlockX() / 16;
        int x2 = from.getBlockX() / 16;
        return x1 != x2 || Math.signum(to.getX()) != Math.signum(from.getX());
    }

}
