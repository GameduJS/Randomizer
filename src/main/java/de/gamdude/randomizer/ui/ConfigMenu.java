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
        ItemBuilder itemCooldownBuilder = new ItemBuilder(Material.CLOCK).setDisplayName("<gray>Item Cooldown").setLore("").setLore("<gray>Determine the cooldown for items to spawn.").addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore("<gray>Current Cooldown: <yellow>" + config.getProperty("itemCooldown").getAsInt() + " seconds");

        inventory.setItem(10, itemCooldownBuilder.build());
        inventory.setItem(28, getCanGetHungryItem());
        inventory.setItem(29, new ItemBuilder(Material.COMPASS)
                .setDisplayName("<gray>Playtime").setLore("").setLore("<gray>Determine the time for the game.")
                .setLore("<gray>Current Time: <yellow>" + TimeConverter.getTimeString(config.getProperty("playTime").getAsInt()) + " seconds")
                .build());
        inventory.setItem(30, getSpawnWithDefaultsItem());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClose(Player player) {

    }

    private ItemStack getSpawnWithDefaultsItem() {
        boolean spawnWithDefaults = config.getProperty("spawnWithDefaults").getAsBoolean();

        return new ItemBuilder(spawnWithDefaults ? Material.OAK_SAPLING : Material.FLOWER_POT).setDisplayName("<gray>Starter Items")
                .setLore("").setLore("<gray>All players will receive a sapling and dirt at the beginning of the round")
                .setLore("<gray>Currently enabled: <yellow>" + spawnWithDefaults).build();
    }

    private ItemStack getCanGetHungryItem() {
        boolean canGetHungry = config.getProperty("canGetHungry").getAsBoolean();
        return new ItemBuilder( canGetHungry ? Material.BEEF : Material.COOKED_BEEF).setDisplayName("<gray>Hunger Option")
                .setLore("").setLore("<gray>Choose whether players should get hungry or not")
                .setLore("<gray>Current Status: <yellow>" + canGetHungry).build();
    }

}
