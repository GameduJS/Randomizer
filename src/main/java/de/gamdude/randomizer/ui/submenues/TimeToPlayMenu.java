package de.gamdude.randomizer.ui.submenues;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.ChildMenu;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

public class TimeToPlayMenu extends ChildMenu {

    private final Config config;
    private int seconds;

    public TimeToPlayMenu(Config config, Menu parentMenu) {
        super(parentMenu, 9, "<yellow>Time To Play");
        this.config = config;
        this.seconds = config.getProperty("playTime").getAsInt();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        if(slot>2) return true;
        int secondsToChange = (int) Math.pow(60, 2 - slot);

        if(type.isRightClick())
            secondsToChange *=-1;
        if(type.isShiftClick())
            secondsToChange*=5;

        seconds = Math.min(60*60*24 - 1, Math.max(0, seconds + secondsToChange));

        inventory.setItem(8,  new ItemBuilder(Material.CLOCK)
                .setDisplayName("<yellow>" + TimeConverter.getTimeString(seconds))
                .setLore("").setLore("<gray>Time to play").build());
        return true;
    }

    @Override
    public void onOpen(Player player) {
        inventory.setItem(8,  new ItemBuilder(Material.CLOCK)
                .setDisplayName("<yellow>" + TimeConverter.getTimeString(seconds))
                .setLore("").setLore("<gray>Time to play").build());

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

    @Override
    public void onClose(Player player) {
        config.addProperty("playTime", seconds);
        super.onClose(player);
    }
}
