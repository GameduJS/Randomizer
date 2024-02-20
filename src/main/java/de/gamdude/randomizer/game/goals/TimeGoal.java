package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.menu.Menu;
import de.gamdude.randomizer.ui.menu.SetTimeValueMenu;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.entity.Player;

public class TimeGoal extends Goal {

    private int seconds;

    public TimeGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher, "playTime", "Time Goal");
    }

    @Override
    public boolean isFinished(GameDispatcher gameDispatcher) {
        return seconds == gameDispatcher.getSecondsPlayed();
    }

    @Override
    public void loadGoalConfig(Config config) {
        this.seconds = config.getProperty(this.configKey).getAsInt();
    }

    @Override
    public String getScoreboardGoalValue(Player player, boolean initial) {
        int time = seconds - ((initial) ? 0 : gameDispatcher.getSecondsPlayed());
        return MessageHandler.getString(player, "scoreboardTimeGoalValue", TimeConverter.getTimeString(time));
    }

    @Override
    public String getScoreboardGoalDescription(Player player) {
        return MessageHandler.getString(player, "scoreboardTimeGoalTitle");
    }

    @Override
    public Menu getConfigMenu(Player player, Menu parent) {
        return new SetTimeValueMenu(config, MessageHandler.getString(player, "configTimeGoalTitle"), configKey, parent).setDisplayStack(() -> Option.PLAY_TIME.getDisplayItem(player));
    }

}
