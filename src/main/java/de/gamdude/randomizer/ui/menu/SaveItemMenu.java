package de.gamdude.randomizer.ui.menu;

import com.google.gson.JsonElement;
import de.gamdude.randomizer.config.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SaveItemMenu extends Menu {

    private final Config config;
    private final String configKey;
    private final List<Material> items;

    public SaveItemMenu(Config config, String configKey, String title, @Nullable Menu parent) {
        super(parent, 27, title);
        this.config = config;
        this.configKey = configKey;
        this.items = config.getProperty(configKey).getAsJsonArray().asList().stream().map(JsonElement::getAsString).map(Material::valueOf).toList();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        return false;
    }

    @Override
    public void onOpen(Player player) {
        for(int i = 0; i < items.size(); ++i) {
            inventory.setItem(i, new ItemStack(items.get(i)));
        }
    }

    @Override
    public void onClose(Player player) {
        String[] arr = Arrays.stream(inventory.getContents()).filter(Objects::nonNull).map(ItemStack::getType).map(Material::name).toArray(String[]::new);
        config.addProperty(configKey, arr);
    }

    @Override
    public boolean allowPlayerInventoryInteraction() {
        return true;
    }
}
