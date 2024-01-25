package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

public class ConfigMenu extends Menu {

    private final GameDispatcher gameDispatcher;
    private final Config config;

    public ConfigMenu(GameDispatcher gameDispatcher) {
        super(54, "<light_purple><bold>CONFIG");
        this.gameDispatcher = gameDispatcher;
        this.config = gameDispatcher.getConfig();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        switch (slot) {
            case 10 -> Option.CHANGE_ITEM_COOLDOWN.openConfigMenu(player, this);
            case 11 -> {
                if(type.isLeftClick()) // change value of current goal
                    Option.CHANGE_GOAL.openConfigMenu(player, this);
                else
                    Option.CHANGE_GOAL.toggleOption(inventory, type, slot); // toggle current goal
            }
            case 28 -> Option.ALLOW_HUNGER.toggleOption(inventory, type, slot);
            case 30 -> Option.ENABLE_START_ITEMS.toggleOption(inventory, type, slot);
            case 31 -> Option.ALLOW_PVP.toggleOption(inventory, type, slot);
            case 33 -> Option.ENABLE_BREAK_BLOCK.toggleOption(inventory, type, slot);
            case 34 -> Option.BLOCK_DROP.toggleOption(inventory, type, slot);
        }
        return true;
    }

    @Override
    public void onOpen(Player player) {
        gameDispatcher.getHandler(GoalHandler.class).reloadConfig(config);

        inventory.setItem(10, Option.CHANGE_ITEM_COOLDOWN.getDisplayItem());
        inventory.setItem(11, Option.CHANGE_GOAL.getDisplayItem());
        inventory.setItem(28, Option.ALLOW_HUNGER.getDisplayItem());
        inventory.setItem(30, Option.ENABLE_START_ITEMS.getDisplayItem());
        inventory.setItem(31, Option.ALLOW_PVP.getDisplayItem());
        inventory.setItem(32, Option.CHANGE_FIRST_DROP_DELAY.getDisplayItem());
        inventory.setItem(33, Option.ENABLE_BREAK_BLOCK.getDisplayItem());
        inventory.setItem(34, Option.BLOCK_DROP.getDisplayItem());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClose(Player player) { }


}
