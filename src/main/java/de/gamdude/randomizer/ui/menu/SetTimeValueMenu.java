package de.gamdude.randomizer.ui.menu;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SetTimeValueMenu extends Menu {

    private final List<Integer> disabledTimeOptions;
    private final String configKey;
    private final Config config;
    private int time;

    private Supplier<ItemStack> displayStackSupplier;

    public SetTimeValueMenu(Config config, String title, String configKey, @Nullable Menu parent) {
        super(parent, 9, title);
        this.disabledTimeOptions = new ArrayList<>();
        this.config = config;
        this.configKey = configKey;
        this.time = config.getProperty(configKey).getAsInt();
        setDisplayStack(() -> new ItemBuilder(Material.FEATHER).setDisplayName("<yellow>" + TimeConverter.getTimeString(time)).build());
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

        inventory.setItem(8, displayStackSupplier.get());
        return true;
    }

    @Override
    public void onOpen(Player player) {
        if (!disabledTimeOptions.contains(0))
            inventory.setItem(0, getTimeUnitStack(player, 0).material(Material.GOLD_BLOCK).build());
        if(!disabledTimeOptions.contains(1))
            inventory.setItem(1 - disabledTimeOptions.size(), getTimeUnitStack(player, 1).material(Material.GOLD_INGOT).build());

        inventory.setItem(2 - disabledTimeOptions.size(), getTimeUnitStack(player, 2).material(Material.GOLD_NUGGET).build());
        inventory.setItem(8, displayStackSupplier.get());
    }

    private ItemBuilder getTimeUnitStack(Player player, int timeUnit) {
        String timeUnitTranslation = MessageHandler.getString(player, "TIME_TRANSLATION");
        return new ItemBuilder(Material.AIR).translatable(player, "IN-/DECREASE_TIME", timeUnitTranslation.split("//")[timeUnit]);
    }

    public SetTimeValueMenu setDisplayStack(Supplier<ItemStack> displayStackSupplier) {
        this.displayStackSupplier = displayStackSupplier;
        return this;
    }

    public SetTimeValueMenu disableHours() {
        this.disabledTimeOptions.add(0);
        return this;
    }

    @SuppressWarnings("unused")
    public SetTimeValueMenu disableMinutes() {
        this.disabledTimeOptions.add(1);
        return this;
    }
}
