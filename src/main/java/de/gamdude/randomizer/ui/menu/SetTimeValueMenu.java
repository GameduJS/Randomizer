package de.gamdude.randomizer.ui.menu;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SetTimeValueMenu extends Menu {

    private final List<Integer> disabledTimeOptions;
    private final String configKey;
    private final Config config;
    private int time;

    public SetTimeValueMenu(Config config, String title, String configKey, @Nullable Menu parent) {
        super(parent, 9, title);
        this.disabledTimeOptions = new ArrayList<>();
        this.config = config;
        this.configKey = configKey;
        this.time = config.getProperty(configKey).getAsInt();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        int clicked = slot - 2 + disabledTimeOptions.size();
        if(clicked > 0)
            return true;

        int secondsToChange = (int) Math.pow(60, (2 - disabledTimeOptions.size()) - slot);

        if(type.isRightClick())
            secondsToChange *=-1;
        if(type.isShiftClick())
            secondsToChange*=5;

        // Interval: [0 ; 23:59:59]
        time = Math.min(60*60*24 - 1, Math.max(0, time + secondsToChange));
        config.addProperty(configKey, time);
        inventory.setItem(8, new ItemBuilder(Material.FEATHER).setDisplayName("<yellow>" + TimeConverter.getTimeString(time)).build());
        return true;
    }

    @Override
    public void onOpen(Player player) {
        if (!disabledTimeOptions.contains(0))
            inventory.setItem(0, new ItemBuilder(Material.GOLD_BLOCK).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Hours")
                    .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                    .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());
        if(!disabledTimeOptions.contains(1))
            inventory.setItem(1 - disabledTimeOptions.size(), new ItemBuilder(Material.GOLD_INGOT).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Minutes")
                    .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                    .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());

        inventory.setItem(2 - disabledTimeOptions.size(), new ItemBuilder(Material.GOLD_NUGGET).setDisplayName("<green>Increase<gray> / <red>Decrease <gray>Seconds")
                .setLore("").setLore("<yellow>Right-Click <gray>to decrease value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());

        inventory.setItem(8, new ItemBuilder(Material.FEATHER).setDisplayName("<yellow>" + TimeConverter.getTimeString(time)).build());
    }

    public SetTimeValueMenu disableHours() {
        this.disabledTimeOptions.add(0);
        return this;
    }

    public SetTimeValueMenu disableMinutes() {
        this.disabledTimeOptions.add(1);
        return this;
    }
}
