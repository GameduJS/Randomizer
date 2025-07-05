package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.game.handler.LeaderboardHandler;
import de.gamdude.randomizer.game.handler.PlayerProgressTracker;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.menu.Menu;
import de.gamdude.randomizer.ui.menu.SetIndividualValueMenu;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.stream.Collectors;

public class UniqueBlockGoal extends Goal{

    private int blockToBuild;

    private final PlatformLoader platformLoader;
    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;

    public UniqueBlockGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher, "uniqueBlock", "Unique Block Goal");
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
    }

    @Override
    public boolean isFinished(GameDispatcher gameDispatcher) {
        return playerProgressTracker.getBlocksBuilt(leaderboardHandler.getTopPlayers().get(0)) >= blockToBuild;
    }

    @Override
    public void loadGoalConfig(Config config) {
        blockToBuild = config.getProperty("uniqueBlock").getAsInt();
    }

    // Score = Bridge Length * Unique Blocks * (1 - Max Single Block Percentage)
    // Or ln(u + 1) * x * ( 1 - e^-x )
    @Override
    public String getScoreboardGoalValue(Player player, boolean initial) {
        if (initial) {
            return blockToBuild + "";
        }

        int length = platformLoader.getPlatform(player.getUniqueId()).getBlocksBuilt();
        int uniqueCount = platformLoader.getPlatform(player.getUniqueId()).getBlocksList().stream().map(Location::getBlock).map(Block::getType).collect(Collectors.toSet()).size();
        player.sendMessage(uniqueCount + " >< " + length);
        int score = (int) Math.ceil(
          uniqueCount * Math.log( length * uniqueCount )
        );
        return String.valueOf(score);
    }

    @Override
    public String getScoreboardGoalDescription(Player player) {
        return "Get as far possible with the most unique blocks";
    }

    @Override
    public Menu getConfigMenu(Player player, @Nullable Menu parent) {
        return new SetIndividualValueMenu(config, "XDDDDD", configKey, parent).setDisplayStackSupplier(() -> Option.BLOCKS_TO_PLACE.getDisplayItem(player));
    }
}
