package de.gamdude.randomizer.commands;

import de.gamdude.randomizer.base.GameDispatcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StateCommand implements CommandExecutor {

    private final GameDispatcher gameDispatcher;

    public StateCommand(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0)
            return false;

        String state = args[0];

        switch (state) {
            case "start" -> gameDispatcher.startGame();
            case "stop" -> gameDispatcher.loadConfig();
            case "pause" -> gameDispatcher.loadConfig();
        }

        return true;
    }
}
