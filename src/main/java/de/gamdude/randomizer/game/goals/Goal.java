package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.menu.Menu;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public abstract class Goal {

    protected final String configKey;
    protected final String displayName;
    protected GameDispatcher gameDispatcher;
    protected Config config;

    public Goal(GameDispatcher gameDispatcher, String configKey, String displayName) {
        this.gameDispatcher = gameDispatcher;
        this.config = gameDispatcher.getConfig();
        this.configKey = configKey;
        this.displayName = displayName;
    }

    public abstract boolean isFinished(GameDispatcher gameDispatcher);

    public abstract void loadGoalConfig(Config config);

    public abstract String getScoreboardGoalValue(Player player, boolean initial);

    public abstract String getScoreboardGoalDescription(Player player);

    public String getDisplayName() {
        return displayName;
    }

    public abstract Menu getConfigMenu(Player player, @Nullable Menu parent);
}
