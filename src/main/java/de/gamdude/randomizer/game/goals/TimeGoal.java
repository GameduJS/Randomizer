package de.gamdude.randomizer.game.goals;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.UUID;

public class TimeGoal extends Goal{

    private int seconds;

    public TimeGoal(GameDispatcher gameDispatcher) {
        super(gameDispatcher, "<yellow>Length <gray>of the race!", "playTime", "Time Goal");
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
    public String getScoreboardGoalValue(UUID uuid) {
        return "<yellow>" + TimeConverter.getTimeString(seconds - gameDispatcher.getSecondsPlayed());
    }

    @Override
    public String getScoreboardGoalDescription(Player player) {
        return MessageHandler.getString(player, "scoreboardTimeGoalTitle");
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        if(slot>2) return true;
        Option.PLAY_TIME.toggleOption(player, inventory, type, slot);
        return true;
    }

    @Override
    public void onOpen(Player player) {
        inventory.setItem(8,  Option.PLAY_TIME.getDisplayItem(player));

        inventory.setItem(0, new ItemBuilder(Material.GRAY_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Hours")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());
        inventory.setItem(1, new ItemBuilder(Material.LIGHT_GRAY_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Minutes")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());
        inventory.setItem(2, new ItemBuilder(Material.WHITE_WOOL).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Seconds")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

}
