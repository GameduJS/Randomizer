package de.gamdude.randomizer.ui;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.base.goals.Goal;
import de.gamdude.randomizer.base.goals.GoalHandler;
import de.gamdude.randomizer.base.goals.TimeGoal;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.ui.submenues.SetValueMenu;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.TimeConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ConfigMenu extends Menu {

    private final GameDispatcher gameDispatcher;
    private final GoalHandler goalHandler;
    private final Config config;

    public ConfigMenu(GameDispatcher gameDispatcher) {
        super(54, "<light_purple><bold>CONFIG");
        this.gameDispatcher = gameDispatcher;
        this.goalHandler = gameDispatcher.getHandler(GoalHandler.class);
        this.config = gameDispatcher.getConfig();
    }

    @Override
    public boolean onClick(Player player, int slot, ClickType type) {
        switch (slot) {
            case 10 -> player.openInventory(new SetValueMenu(this, config, "itemCooldown").getInventory());
            case 11 -> {
                if(type.isRightClick()) {// change goal
                    int goalId = config.getProperty("currentGoal").getAsInt();
                    int nextGoal = (goalId + 1) % goalHandler.GOALS.length;
                    goalHandler.setActiveGoal(nextGoal);
                    inventory.setItem(11, getGoalItem());
                }

                if(type.isLeftClick()) { // change value
                    Goal goal = goalHandler.getActiveGoal();
                    goal.openMenu(this, player);
                }
            }
            case 28 -> {
                boolean canGetHungry = config.getProperty("canGetHungry").getAsBoolean();
                config.addProperty("canGetHungry", !canGetHungry);
                inventory.setItem(28, getCanGetHungryItem());
            }
            case 30 -> {
                boolean b = config.getProperty("spawnWithDefaults").getAsBoolean();
                config.addProperty("spawnWithDefaults", !b);
                inventory.setItem(30, getSpawnWithDefaultsItem());
            }
            case 31 -> {
                config.addProperty("allowPVP", !config.getProperty("allowPVP").getAsBoolean());
                inventory.setItem(31, getAllowPVPItem());
            }
        }
        return true;
    }

    @Override
    public void onOpen(Player player) {
        gameDispatcher.getHandler(GoalHandler.class).reloadConfig(config);

        ItemBuilder itemCooldownBuilder = new ItemBuilder(Material.CLOCK).setDisplayName("<gray>Item Cooldown").setLore("").setLore("<gray>Determine the cooldown for items to spawn.").addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore("<gray>Current Cooldown: <yellow>" + config.getProperty("itemCooldown").getAsInt() + " seconds");

        inventory.setItem(10, itemCooldownBuilder.build());
        inventory.setItem(11, getGoalItem());
        inventory.setItem(28, getCanGetHungryItem());
        inventory.setItem(30, getSpawnWithDefaultsItem());
        inventory.setItem(31, getAllowPVPItem());

        fill(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClose(Player player) {

    }

    private ItemStack getSpawnWithDefaultsItem() {
        boolean spawnWithDefaults = config.getProperty("spawnWithDefaults").getAsBoolean();

        return new ItemBuilder(spawnWithDefaults ? Material.OAK_SAPLING : Material.FLOWER_POT).setDisplayName("<gray>Starter Items")
                .setLore("").setLore("<gray>All players will receive a sapling and dirt at the beginning of the round")
                .setLore("<gray>Currently Enabled: <yellow>" + spawnWithDefaults).build();
    }

    private ItemStack getCanGetHungryItem() {
        boolean canGetHungry = config.getProperty("canGetHungry").getAsBoolean();
        return new ItemBuilder( canGetHungry ? Material.BEEF : Material.COOKED_BEEF).setDisplayName("<gray>Hunger Option")
                .setLore("").setLore("<gray>Choose whether players should get hungry or not")
                .setLore("<gray>Current Status: <yellow>" + canGetHungry).build();
    }

    private ItemStack getAllowPVPItem() {
        boolean allowPVP = config.getProperty("allowPVP").getAsBoolean();
        return new ItemBuilder( allowPVP ? Material.DIAMOND_SWORD : Material.WOODEN_SHOVEL).setDisplayName("<gray>Allow PVP")
                .setLore("").setLore("<gray>Control whether players can attack each other").setLore("<gray>Currently Enabled: <yellow>" + allowPVP).build();
    }

    private ItemStack getGoalItem() {
        Goal goal = goalHandler.getActiveGoal();
        return new ItemBuilder(goal.getClass() == TimeGoal.class ? Material.COMPASS : Material.GRASS_BLOCK).setDisplayName("<gray>Goal Of The Game")
                .setLore("").setLore("<yellow>Right-Click <gray>to change the goal for the game").setLore("<yellow>Left-Click <gray>to change the amount of the goal").setLore("").setLore("<gray>Current Goal: <yellow>" + goal.getDisplayName()).build();
    }

    private ItemStack getDayNightCycleItem() {
        boolean doDayNightCycle = config.getProperty("doDayNightCycle").getAsBoolean();
        return new ItemBuilder(Material.CLOCK).setDisplayName("<gray>DayNightCycle Options")
                .setLore("").setDisplayName("<gray>Choose whether the time should change")
                .setLore("<gray>Current Status: <yellow>" + doDayNightCycle).build();
    }

}
