package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.game.handler.LeaderboardHandler;
import de.gamdude.randomizer.game.handler.PlayerProgressTracker;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.menu.Menu;
import de.gamdude.randomizer.ui.menu.SetIndividualValueMenu;
import de.gamdude.randomizer.utils.MessageHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BlockGoal extends Goal {

    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;
    private int blocksToBuild;

    public BlockGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher, "blockToGoal", "Block Goal");
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
    }

    @Override
    public boolean isFinished(GameDispatcher gameDispatcher) {
        return blocksToBuild <= playerProgressTracker.getBlocksBuilt(leaderboardHandler.getTopPlayers().get(0));
    }

    @Override
    public void loadGoalConfig(Config config) {
        blocksToBuild = config.getProperty("blockToGoal").getAsInt();
    }

    @Override
    public String getScoreboardGoalValue(Player player, boolean initial) {
        return MessageHandler.getString(player, "scoreboardBlockGoalValue", blocksToBuild + "");
    }

    @Override
    public String getScoreboardGoalDescription(Player player) {
        return MessageHandler.getString(player, "scoreboardBlockGoalTitle");
    }

    @Override
    public Menu getConfigMenu(Player player, @Nullable Menu parent) {
        return new SetIndividualValueMenu(config, MessageHandler.getString(player, "configBlockGoalTitle"), configKey, parent).setDisplayStackSupplier(() -> Option.BLOCKS_TO_PLACE.getDisplayItem(player));
    }
}
