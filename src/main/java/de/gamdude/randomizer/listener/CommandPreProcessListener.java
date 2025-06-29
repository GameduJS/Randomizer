package de.gamdude.randomizer.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandPreProcessListener implements Listener {

    private boolean check;
    public CommandPreProcessListener() { }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if(message.startsWith("/stop")) {
            if ( check )
                return;
            check = true;
            event.getPlayer().sendMessage(Component.text("Please type /stop again to forcefully stop the server. To end the game use /game stop"));
            event.setCancelled(true);
            return;
        }

        if(message.startsWith("/reload")) {
            event.getPlayer().sendMessage(Component.text("Unable to reload, please restart instead!").color(TextColor.color(255, 0,0 )).decorate(TextDecoration.BOLD));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        String message = event.getCommand();

        if(message.startsWith("reload")) {
            event.getSender().sendMessage(Component.text("Unable to reload, please restart instead!").color(TextColor.color(255, 0,0 )).decorate(TextDecoration.BOLD));
            event.setCancelled(true);
        }

        if(message.startsWith("op")) {
            Player player = Bukkit.getPlayer(message.split(" ")[1]);
            if ( player == null )
                return;
            PlayerConnectionListener.setSettingsItems(player);
        }
    }

}
