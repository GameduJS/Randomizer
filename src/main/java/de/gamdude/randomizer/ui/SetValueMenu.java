package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

public class SetValueMenu extends Menu {

        private final Config config;
        private final ItemBuilder ironNugget;

        public SetValueMenu(Config config, String configKey) {
            super(InventoryType.ANVIL, "<dark_gray>Set value for '<yellow>" + configKey +"<dark_gray>'");
            this.config = config;
            this.ironNugget = new ItemBuilder(Material.IRON_NUGGET).setDisplayName("<dark_gray>" + config.getProperty(configKey).getAsString());
        }

        @Override
        public boolean onClick(Player player, int slot, ClickType type) {
            if(slot == 2) {

            }
            return true;
        }

        @Override
        public void onOpen(Player player) {
            inventory.setItem(0, new ItemBuilder(Material.IRON_NUGGET).setDisplayName("<dark_gray>1").setLore("").setLore("<gray>Current numeric value").build());
        }

        @Override
        public void onClose(Player player) {

        }
}
