package de.gamdude.randomizer.ui.submenues;

import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.ChildMenu;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class SetValueMenu extends ChildMenu {

        private final Config config;
        private final String configKey;
        private final ItemBuilder ironNugget;
        private long number;

        public SetValueMenu(Menu parent, Config config, String configKey) {
            super(parent,9,  "<dark_gray>Set value for '<yellow>" + configKey +"<dark_gray>'");
            this.config = config;
            this.configKey = configKey;
            this.number = config.getProperty(configKey).getAsLong();
            this.ironNugget = new ItemBuilder(Material.IRON_NUGGET).setDisplayName("<dark_gray>" + number);
        }

        @Override
        public boolean onClick(Player player, int slot, ClickType type) {
            int step = (type == ClickType.SHIFT_LEFT || type == ClickType.SHIFT_RIGHT) ? 5 : 1;
            if(slot == 0) {
                number = Math.max(1, number - step);
                config.addProperty(configKey, number);
            }
            if(slot == 8)
                config.addProperty(configKey, number+=step);
            inventory.setItem(4, ironNugget.setDisplayName("<dark_gray>" + number).build());
            return true;
        }

        @Override
        public void onOpen(Player player) {
            inventory.setItem(0, new ItemBuilder(Material.RED_WOOL).setDisplayName("<red>Decrease value").build());
            inventory.setItem(4, ironNugget.build());
            inventory.setItem(8, new ItemBuilder(Material.LIME_WOOL).setDisplayName("<green>Increase value").build());
        }
}
