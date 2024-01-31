package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.LeaderboardHandler;
import de.gamdude.randomizer.base.PlayerProgressTracker;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.UUID;

public class BlockGoal extends Goal {

    private final PlayerProgressTracker playerProgressTracker;
    private final LeaderboardHandler leaderboardHandler;
    private int blocksToBuild;

    public BlockGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher,"<yellow>Blocks <gray>to be built!", "blockToGoal", "Block Goal");
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
    public String getScoreboardGoalValue(UUID uuid) {
        return "<yellow>" + (blocksToBuild);
    }

    @Override
    public String getScoreboardGoalDescription(Player player) {
        return MessageHandler.getString(player, "scoreboardBlockGoalTitle");
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        if(slot - 1 > 0)
            return true;
        Option.BLOCKS_TO_PLACE.toggleOption(player, inventory, type, slot);
        return true;
    }

    @Override
    public void onOpen(Player player) {
        inventory.setItem(8, Option.BLOCKS_TO_PLACE.getDisplayItem(player));

        inventory.setItem(0, new ItemBuilder(Material.WHITE_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value by <yellow>1").setLore("<yellow>Left-Click <gray>to increase value by <yellow>1").build());
        inventory.setItem(1, new ItemBuilder(Material.GRAY_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value by <yellow>50").setLore("<yellow>Left-Click <gray>to increase value by <yellow>50").build());
    }

}
