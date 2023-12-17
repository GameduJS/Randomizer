package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.ui.submenues.SetValueMenu;
import de.gamdude.randomizer.ui.submenues.TimeToPlayMenu;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ConfigMenu extends Menu {

    private final Config config;

    public ConfigMenu(GameDispatcher gameDispatcher) {
        super(54, "<light_purple><bold>CONFIG");
        this.config = gameDispatcher.getConfig();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        switch (slot) {
            case 10 -> player.openInventory(new SetValueMenu(this, config, "itemCooldown").getInventory());
            case 28 -> {
                boolean canGetHungry = config.getProperty("canGetHungry").getAsBoolean();
                config.addProperty("canGetHungry", !canGetHungry);
                inventory.setItem(28, getCanGetHungryItem());
            }
            case 29 -> player.openInventory(new TimeToPlayMenu(config, this).getInventory());
            case 30 -> {
                boolean b = config.getProperty("spawnWithDefaults").getAsBoolean();
                config.addProperty("spawnWithDefaults", !b);
                inventory.setItem(30, getSpawnWithDefaultsItem());
            }
        }
        return true;
    }

    @Override
    public void onOpen(Player player) {
        ItemBuilder itemCooldownBuilder = new ItemBuilder(Material.CLOCK).setDisplayName("<dark_gray>Item Cooldown").setLore("").setLore("<gray>Set value for the").setLore("<gray>item cooldown").addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore("<gray>Current value: <yellow>" + config.getProperty("itemCooldown").getAsInt());

        inventory.setItem(10, itemCooldownBuilder.build());
        inventory.setItem(28, getCanGetHungryItem());
        inventory.setItem(29, new ItemBuilder(Material.COMPASS)
                .setDisplayName("<dark_gray>Play Time").setLore("").setLore("<gray>Sets the value for the game")
                .setLore("<gray>Current: <yellow>" + TimeConverter.getTimeString(config.getProperty("playTime").getAsInt()))
                .build());
        inventory.setItem(30, getSpawnWithDefaultsItem());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClose(Player player) {

    }

    private ItemStack getSpawnWithDefaultsItem() {
        boolean spawnWithDefaults = config.getProperty("spawnWithDefaults").getAsBoolean();

        if(spawnWithDefaults)
            return new ItemBuilder(Material.OAK_SAPLING).setDisplayName("<green>Enabled <gray>Starter Items").setLore("")
                    .setLore("<gray>All players will receive a sapling and dirt at the beginning of the round").build();
        else
            return new ItemBuilder(Material.FLOWER_POT).setDisplayName("<red>Disabled <gray>Starter Items").setLore("")
                    .setLore("<gray>All players will receive a sapling and dirt at the beginning of the round").build();
    }

    private ItemStack getCanGetHungryItem() {
        boolean canGetHungry = config.getProperty("canGetHungry").getAsBoolean();

        if(!canGetHungry) // false
            return new ItemBuilder(Material.COOKED_BEEF).setDisplayName("<red>Disabled <dark_gray>Hunger")
                    .setLore("").setLore("<gray>Click to <green>enable").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
        else
            return new ItemBuilder(Material.BEEF).setDisplayName("<green>Enabled <dark_gray>Hunger")
                    .setLore("").setLore("<gray>Click to <red>disable").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build();
    }

}
