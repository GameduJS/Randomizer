package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ConfigMenu extends Menu {

    private final GameDispatcher gameDispatcher;

    public ConfigMenu(GameDispatcher gameDispatcher) {
        super(54, "<light_purple><bold>CONFIG");
        this.gameDispatcher = gameDispatcher;
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        switch (slot) {
            case 10 -> player.openInventory(new SetValueMenu(this, gameDispatcher.getConfig(), "itemCooldown").getInventory());
            case 28 -> {
                boolean canGetHungry = gameDispatcher.getConfig().getProperty("canGetHungry").getAsBoolean();
                gameDispatcher.getConfig().addProperty("canGetHungry", !canGetHungry);
                inventory.setItem(28, getCanGetHungryItem());
            }
        }
        return true;
    }

    @Override
    public void onOpen(Player player) {
        ItemBuilder itemCooldownBuilder = new ItemBuilder(Material.CLOCK).setDisplayName("<dark_gray>Item Cooldown").setLore("").setLore("<gray>Set value for the").setLore("<gray>item cooldown").addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore("<gray>Current value: <yellow>" + gameDispatcher.getConfig().getProperty("itemCooldown").getAsInt());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
        inventory.setItem(10, itemCooldownBuilder.build());
        inventory.setItem(28, getCanGetHungryItem());
    }

    @Override
    public void onClose(Player player) {

    }

    public ItemStack getCanGetHungryItem() {
        boolean canGetHungry = gameDispatcher.getConfig().getProperty("canGetHungry").getAsBoolean();

        if(!canGetHungry) // false
            return new ItemBuilder(Material.COOKED_BEEF).setDisplayName("<red>Disabled <dark_gray>hunger")
                    .setLore("").setLore("<gray>Click to <green>enable <gray>hunger").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
        else
            return new ItemBuilder(Material.BEEF).setDisplayName("<green>Enabled <dark_gray>hunger")
                    .setLore("").setLore("<gray>Click to <red>disable <gray>hunger").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
    }

}
