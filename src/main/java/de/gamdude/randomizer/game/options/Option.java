package de.gamdude.randomizer.game.options;

import com.google.gson.JsonElement;
import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.goals.Goal;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.game.goals.TimeGoal;
import de.gamdude.randomizer.ui.base.Menu;
import de.gamdude.randomizer.ui.submenues.SetValueMenu;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.TimeConverter;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public enum Option {
    //<editor-fold desc="Default Options", defaultState=closed>
    ENABLE_DAY_NIGHT_CYCLE("doDayNightCycle", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.NETHER_STAR).setDisplayName("<dark_gray><b>Day/Night Cycle")
                    .setLore("").setLore("<gray>Choose whether the time should change")
                    .setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    },

    ENABLE_PVP("allowPVP", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean allowPVP = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder( allowPVP ? Material.DIAMOND_SWORD : Material.WOODEN_SHOVEL).setDisplayName("<dark_gray><b>PVP Option")
                    .setLore("").setLore("<gray>Control whether players can attack each other").setLore("").setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    },

    ENABLE_HUNGER("canGetHungry", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean canGetHungry = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder( canGetHungry ? Material.BEEF : Material.COOKED_BEEF).setDisplayName("<dark_gray><b>Hunger Option")
                    .setLore("").setLore("<gray>Choose whether players should get hungry or not").setLore("")
                    .setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    },

    ENABLE_START_ITEMS("spawnWithDefaults", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean spawnWithDefaults = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder(spawnWithDefaults ? Material.OAK_SAPLING : Material.FLOWER_POT).setDisplayName("<dark_gray><b>Starter Items")
                    .setLore("").setLore("<gray>All players will receive a sapling and dirt at the beginning of the round").setLore("")
                    .setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    },

    ENABLE_BREAK_BLOCK("canBreakBlock", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.STONE_PICKAXE).setDisplayName("<dark_gray><b>Block Break").setLore("")
                    .setLore("<gray>Choose whether players are able to destroy blocks").setLore("")
                    .setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    },

    CHANGE_ITEM_COOLDOWN("itemCooldown", 10) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return  new ItemBuilder(Material.CLOCK).setDisplayName("<dark_gray><b>Item Cooldown").setLore("").setLore("<gray>Determine the cooldown for items to spawn.").setLore("").addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .setLore("<gray>Current Cooldown: <yellow>" + config.getProperty(this.configKey).getAsInt() + " seconds").build();
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            player.openInventory(new SetValueMenu(parent, config, configKey).getInventory());
        }
    },

    CHANGE_GOAL("currentGoal", 0) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            Goal goal = gameDispatcher.getHandler(GoalHandler.class).getActiveGoal();
            return new ItemBuilder(goal.getClass() == TimeGoal.class ? Material.COMPASS : Material.GRASS_BLOCK).setDisplayName("<dark_gray><b>Goal")
                    .setLore("").setLore("<yellow>Right-Click <gray>to change the goal of the game").setLore("<yellow>Left-Click <gray>to change setting of the goal").setLore("").setLore("<gray>Current Goal: <yellow>" + goal.getDisplayName())
                    .setLore("<gray>Goal Value: <yellow>" + goal.getScoreboardGoalValue(player.getUniqueId())).build();
        }

        @Override
        public void toggleOption(Player player, Inventory inventory, ClickType type, int slot) {
            GoalHandler goalHandler = gameDispatcher.getHandler(GoalHandler.class);
            if(type.isRightClick()) {
                int goalId = config.getProperty(configKey).getAsInt();
                int nextGoal = (goalId + 1) % goalHandler.GOALS.length;
                goalHandler.setActiveGoal(nextGoal);
                inventory.setItem(slot, getDisplayItem(player));
            }
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            gameDispatcher.getHandler(GoalHandler.class).getActiveGoal().openMenu(parent, player);
        }
    },

    CHANGE_FIRST_DROP_DELAY("firstItemDropDelay", 5) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.REPEATER).setDisplayName("<dark_gray><b>First Delay").setLore("")
                    .setLore("<gray>Decide how much delay you want to have between start and the first item drop")
                    .setLore("").setLore("<gray>Current delay: <yellow>" + TimeConverter.getTimeString(getValue().getAsInt())).build();
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            player.openInventory(new SetValueMenu(parent, config, configKey).getInventory());
        }
    },

    PLAY_TIME("playTime", 900) {
        @Override
        public void toggleOption(Player player, Inventory inventory, ClickType type, int slot) {
            int seconds = getValue().getAsInt();
            int secondsToChange = (int) Math.pow(60, 2 - slot);

            if(type.isRightClick())
                secondsToChange *=-1;
            if(type.isShiftClick())
                secondsToChange*=5;

            seconds = Math.min(60*60*24 - 1, Math.max(0, seconds + secondsToChange));
            config.addProperty(configKey, seconds);
            inventory.setItem(8,  getDisplayItem(player));
        }

        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.CLOCK)
                    .setDisplayName("<dark_gray><b>Play Time")
                    .setLore("").setLore("<gray>Change the length of the game").setLore("")
                    .setLore("<gray>Current length: <yellow>" + TimeConverter.getTimeString(getValue().getAsInt())).build();
        }
    },

    BLOCKS_TO_PLACE("blockToGoal", 100) {
        @Override
        public void toggleOption(Player player, Inventory inventory, ClickType type, int slot) {
            int blocksToBuild = getValue().getAsInt();
            int factor = (int) Math.pow(50, slot);
            if(type.isRightClick())
                factor*=-1;

            blocksToBuild = Math.max(1, blocksToBuild + factor);
            config.addProperty(configKey, blocksToBuild);
            inventory.setItem(8,  getDisplayItem(player));
        }

        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.GRASS_BLOCK)
                    .setDisplayName("<dark_gray><b>Blocks to Victory")
                    .setLore("").setLore("<gray>Total number of blocks a player must build to achieve victory.").setLore("")
                    .setLore("<gray>Current value: <yellow>" + getValue().getAsInt()).build();
        }
    },

    EXCLUDED_ITEMS("excludedItems", new String[]{"LIGHT"}) {

    },

    BLOCK_DROP("doBlockDrop", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.COBBLESTONE).setDisplayName("<dark_gray><b>Block Drops")
                    .setLore("").setLore("<gray>Choose whether blocks should drop after being broken.").setLore("")
                    .setLore("<gray>Currently <yellow>" + getStatusString()).build();
        }
    };
    //</editor-fold>

    private static GameDispatcher gameDispatcher;
    private static Config config;
    public final String configKey;
    public final Object defaultValue;

    Option(String configKey, Object defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    public static void injectGameDispatcher(GameDispatcher injectedGameDispatcher) {
        gameDispatcher = injectedGameDispatcher;
        config = injectedGameDispatcher.getConfig();
    }

    public ItemStack getDisplayItem(Player player) {
        return null;
    }

    public void toggleOption(Player player, Inventory inventory, ClickType type, int slot) {
        boolean value = config.getProperty(configKey).getAsBoolean();
        config.addProperty(configKey, !value);
        inventory.setItem(slot, getDisplayItem(player));
    }

    public JsonElement getValue() {
        return config.getProperty(configKey);
    }

    public void openConfigMenu(Player player,  @Nullable Menu parent) {
        throw new NotImplementedException("Menu hasn't been implemented for this option");
    }

    protected String getStatusString() {
        return getValue().getAsBoolean() ? "Enabled" : "Disabled";
    }
}
