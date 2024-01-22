package de.gamdude.randomizer.base.goals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.LeaderboardHandler;
import de.gamdude.randomizer.base.PlayerProgressTracker;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.UUID;

public class BlockGoal extends Goal {

    private final Config config;
    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;
    private int blocksToBuild;

    public BlockGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher,"blocksToGoal", "Block Goal");
        this.config = gameDispatcher.getConfig();
        this.playerProgressTracker = gameDispatcher.getHandler(PlayerProgressTracker.class);
        this.leaderboardHandler = gameDispatcher.getHandler(LeaderboardHandler.class);
    }

    @Override
    public boolean isFinished(GameDispatcher gameDispatcher) {
        return blocksToBuild == playerProgressTracker.getBlocksBuilt(leaderboardHandler.getTopPlayers().getPlayerList().get(0));
    }

    @Override
    public void loadConfig(Config config) {
        blocksToBuild = config.getProperty("blocksToGoal").getAsInt();
    }

    @Override
    public String getScoreboardGoalValue(UUID uuid) {
        return "<yellow>" + (blocksToBuild);
    }

    @Override
    public String getScoreboardGoalTitle(Player player) {
        return MessageHandler.getString(player, "scoreboardBlockGoalTitle");
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        if(slot - 1 > 0)
            return true;

        int factor = (int) Math.pow(50, slot);
        if(type.isRightClick())
            factor*=-1;

        blocksToBuild = Math.max(0, blocksToBuild + factor);
        config.addProperty(configKey, blocksToBuild);

        inventory.setItem(8,  new ItemBuilder(Material.GRASS_BLOCK)
                .setDisplayName("<yellow>" + blocksToBuild)
                .setLore("").setLore("<gray>Total number of blocks a player must build to win the game").build());
        return true;
    }

    @Override
    public void onOpen(Player player) {
        inventory.setItem(8,  new ItemBuilder(Material.GRASS_BLOCK)
                .setDisplayName("<yellow>" + blocksToBuild)
                .setLore("").setLore("<gray>Total number of blocks a player must build to win the game").build());

        inventory.setItem(0, new ItemBuilder(Material.WHITE_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value by <yellow>1").setLore("<yellow>Left-Click <gray>to increase value by <yellow>1").build());
        inventory.setItem(1, new ItemBuilder(Material.GRAY_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value by <yellow>50").setLore("<yellow>Left-Click <gray>to increase value by <yellow>50").build());
    }

}
