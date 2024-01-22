package de.gamdude.randomizer.ui.submenues;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.ChildMenu;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SetValueMenu extends ChildMenu {

        private final Config config;
        private final String configKey;
        private long number;

        public SetValueMenu(Menu parent, Config config, String configKey) {
            super(parent,9,  "<gray>Set value for '<yellow>" + configKey +"<gray>'");
            this.config = config;
            this.configKey = configKey;
            this.number = config.getProperty(configKey).getAsLong();
        }

        @Override
        public boolean onClick(Player player, int slot, ClickType type) {
            int step = type.isShiftClick() ? 5 : 1;

            if(slot == 8)
                number = Math.max(1, number - step);
            if(slot == 0)
                number+=step;
            config.addProperty(configKey, number);

            inventory.setItem(4, getItem());
            return true;
        }

        @Override
        public void onOpen(Player player) {
            this.inventory.setItem(0, new ItemBuilder(Material.LIME_WOOL).setDisplayName("<green>Increase <gray>Value")
                    .setLore("").setLore("<yellow>Click <gray>to increase value").setLore("<yellow>Left-Click <gray>to increase value").setLore("")
                    .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());
            this.inventory.setItem(8, new ItemBuilder(Material.RED_WOOL).setDisplayName("<red>Decrease <gray>Value")
                    .setLore("").setLore("<yellow>Click <gray>to decrease value").setLore("")
                    .setLore("<gray>Press <yellow>Shift <gray>to change by 5").build());

            this.inventory.setItem(4, getItem());
        }

        private ItemStack getItem() {
            return new ItemBuilder(Material.IRON_NUGGET).setDisplayName("<gray>Value of '<yellow>" + configKey + "<gray>': <yellow>" + number).build();
        }
}
