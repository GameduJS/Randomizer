package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.Menu;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Goal extends Menu {

    protected final String configKey;
    protected final String displayName;
    protected Menu parentMenu;
    protected GameDispatcher gameDispatcher;

    public Goal(GameDispatcher gameDispatcher, String inventoryTitle, String configKey, String displayName) {
        super(9, inventoryTitle);
        this.gameDispatcher = gameDispatcher;
        this.configKey = configKey;
        this.displayName = displayName;
    }

    public void openMenu(Menu parentMenu, Player player) {
        player.openInventory(getInventory());
        this.parentMenu = parentMenu;
    }

    @Override
    public void onClose(Player player) {
        player.openInventory(parentMenu.getInventory());
    }

    public abstract boolean isFinished(GameDispatcher gameDispatcher);

    public abstract void loadGoalConfig(Config config);

    public abstract String getScoreboardGoalValue(UUID uuid);

    public abstract String getScoreboardGoalDescription(Player player);

    public String getDisplayName() {
        return displayName;
    }
}
