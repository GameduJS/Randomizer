package de.gamdude.randomizer.commands;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.ui.menu.ConfigMenu;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameCommand implements TabExecutor {

    private final GameDispatcher gameDispatcher;

    public GameCommand(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 1)
            return false;
        String state = args[0];

        switch (state) {
            case "setting" -> ((Player) sender).openInventory(new ConfigMenu(gameDispatcher).getInventory());
            case "start" -> gameDispatcher.startGame();
            case "stop" -> gameDispatcher.stopGame();
            case "pause" -> gameDispatcher.pause();
        }

        return true;
    }

    private static final String[] COMMANDS = {"setting", "start", "stop", "pause"};

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if(args.length == 1)
            StringUtil.copyPartialMatches(args[0], List.of(COMMANDS), completions);
        return completions;
    }
}
