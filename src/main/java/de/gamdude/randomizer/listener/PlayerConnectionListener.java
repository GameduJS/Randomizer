package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.world.Platform;
import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;

public class PlayerConnectionListener implements Listener {

    private final PlatformLoader platformLoader;
    private final GameDispatcher gameDispatcher;

    public PlayerConnectionListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MessageHandler.registerLanguage(player);

        event.joinMessage(null);
        Bukkit.broadcast(MessageHandler.getMessage(player, "joinMessage", player.getName()));

        // Players that are too late won't have a platform
        // Players who rejoin will get back to their platform
        if(gameDispatcher.getState() != 0 && platformLoader.getPlatform(player.getUniqueId()) == null) {
            player.setGameMode(GameMode.SPECTATOR);
            MessageHandler.sendMessage(player, "playerTooLate");
            return;
        }

        Platform platform = platformLoader.createPlatform(player.getUniqueId(), Bukkit.getOnlinePlayers().size());
        platform.setEnabled(true);
        player.teleport(platform.getPlatformLocation());
        gameDispatcher.getRandomizerScoreboard().setScoreboard(player);

       if(gameDispatcher.getState() == 0 && Bukkit.getOperators().size() == 1 && player.isOp())
           setSettingsItems(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);
        Bukkit.broadcast(MessageHandler.getMessage(player, "leaveMessage", player.getName()));

        if(gameDispatcher.getState() == 1) {
            Platform platform = platformLoader.getPlatform(player.getUniqueId());
            if(platform != null)
                platform.setEnabled(false);
        }
    }

    public static void setSettingsItems(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.COMMAND_BLOCK).setDisplayName("<red><b>SETTINGS").setLore("").setLore("<gray>Change the settings of the game").addData("settings-item").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
        player.getInventory().setItem(1, new ItemBuilder(Material.LIME_WOOL).setDisplayName("<green><b>START").setLore("").setLore("<gray>Start the game!").addData("start-item").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

}
