package de.gamdude.randomizer.ui.menu;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SetIndividualValueMenu extends Menu {

    private final Config config;
    private Supplier<ItemStack> displayStackSupplier;
    private final String configKey;
    private int value;

    public SetIndividualValueMenu(Config config, String title, String configKey, @Nullable Menu parent) {
        super(parent, 9, title);
        this.config = config;
        this.configKey = configKey;
        this.value = config.getProperty(configKey).getAsInt();

        this.displayStackSupplier = () -> new ItemBuilder(Material.FEATHER).setDisplayName("<yellow>" + value).build();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        if(slot - 2 > 0)
            return true;

        int factor = (int) Math.pow(50, 1 - slot);
        if(type.isRightClick())
            factor*=-1;

        value = Math.max(1, value + factor);
        config.addProperty(configKey, value);

        inventory.setItem(8, displayStackSupplier.get());
        return true;
    }

    @Override
    public void onOpen(Player player) {
        inventory.setItem(0, new ItemBuilder(Material.GRAY_WOOL).translatable(player, "IN-/DECREASE_VALUE", "50", "50").build());
        inventory.setItem(1, new ItemBuilder(Material.WHITE_WOOL).translatable(player, "IN-/DECREASE_VALUE", "1",  "1").build());

        inventory.setItem(8, displayStackSupplier.get());
    }

    public SetIndividualValueMenu setDisplayStackSupplier(Supplier<ItemStack> displayStackSupplier) {
        this.displayStackSupplier = displayStackSupplier;
        return this;
    }
}
