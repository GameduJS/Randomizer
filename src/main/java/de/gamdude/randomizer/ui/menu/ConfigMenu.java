package de.gamdude.randomizer.ui.menu;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

public class ConfigMenu extends Menu {

    private final GameDispatcher gameDispatcher;
    private final Config config;

    public ConfigMenu(GameDispatcher gameDispatcher) {
        super(4*9, "<light_purple><bold>CONFIG");
        this.gameDispatcher = gameDispatcher;
        this.config = gameDispatcher.getConfig();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        switch (slot) {
            case 19 -> Option.CHANGE_ITEM_COOLDOWN.openConfigMenu(player, this);
            case 13 -> {
                if(type.isLeftClick()) // change value of current goal
                    Option.CHANGE_GOAL.openConfigMenu(player, this);
                else
                    Option.CHANGE_GOAL.toggleOption(player, inventory, type, slot); // toggle current goal
            }
            case 25 -> Option.ENABLE_HUNGER.toggleOption(player, inventory, type, slot);
            case 21 -> Option.ENABLE_START_ITEMS.toggleOption(player, inventory, type, slot);
            case 22 -> Option.ENABLE_PVP.toggleOption(player, inventory, type, slot);
            case 23 -> Option.ENABLE_BREAK_BLOCK.toggleOption(player, inventory, type, slot);
            case 24 -> Option.BLOCK_DROP.toggleOption(player, inventory, type, slot);
            case 20 -> Option.CHANGE_FIRST_DROP_DELAY.openConfigMenu(player, this);
        }
        return true;
    }

    @Override
    public void onOpen(Player player) {
        gameDispatcher.getHandler(GoalHandler.class).reloadConfig(config);

        inventory.setItem(13, Option.CHANGE_GOAL.getDisplayItem(player));
        inventory.setItem(19, Option.CHANGE_ITEM_COOLDOWN.getDisplayItem(player));

        inventory.setItem(20, Option.CHANGE_FIRST_DROP_DELAY.getDisplayItem(player));
        inventory.setItem(21, Option.ENABLE_START_ITEMS.getDisplayItem(player));
        inventory.setItem(22, Option.ENABLE_PVP.getDisplayItem(player));
        inventory.setItem(23, Option.ENABLE_BREAK_BLOCK.getDisplayItem(player));
        inventory.setItem(24, Option.BLOCK_DROP.getDisplayItem(player));
        inventory.setItem(25, Option.ENABLE_HUNGER.getDisplayItem(player));

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClose(Player player) { }


}
