package de.gamdude.randomizer.game.options;

import com.google.gson.JsonElement;
import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.goals.Goal;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.game.goals.TimeGoal;
import de.gamdude.randomizer.ui.menu.Menu;
import de.gamdude.randomizer.ui.menu.SaveItemMenu;
import de.gamdude.randomizer.ui.menu.SetTimeValueMenu;
import de.gamdude.randomizer.utils.ItemBuilder;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.utils.TimeConverter;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public enum Option {
    //<editor-fold desc="Default Options", defaultState=closed>
    ENABLE_DAY_NIGHT_CYCLE("doDayNightCycle", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.NETHER_STAR).translatable(player, "DAY_NIGHT_CYCLE", getStatusString(player)).build();
        }
    },

    ENABLE_PVP("allowPVP", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean allowPVP = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder(allowPVP ? Material.DIAMOND_SWORD : Material.WOODEN_SHOVEL).translatable(player, "ENABLE_PVP", getStatusString(player)).build();
        }
    },

    ENABLE_HUNGER("canGetHungry", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean canGetHungry = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder( canGetHungry ? Material.ROTTEN_FLESH : Material.COOKED_BEEF).translatable(player, "ENABLE_HUNGER", getStatusString(player)).build();
        }
    },

    ENABLE_START_ITEMS("spawnWithDefaults", true) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean spawnWithDefaults = config.getProperty(this.configKey).getAsBoolean();
            return new ItemBuilder(spawnWithDefaults ? Material.OAK_SAPLING : Material.FLOWER_POT).translatable(player, "ENABLE_STARTER_ITEMS", getStatusString(player)).build();
        }
    },

    ENABLE_BREAK_BLOCK("canBreakBlock", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean val = getValue().getAsBoolean();
            return new ItemBuilder(val ? Material.DIAMOND_PICKAXE : Material.WOODEN_PICKAXE).translatable(player, "ENABLE_BLOCK_BREAK", getStatusString(player)).build();
        }
    },

    CHANGE_ITEM_COOLDOWN("itemCooldown", 10) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return  new ItemBuilder(Material.CLOCK).translatable(player, "CHANGE_ITEM_COOLDOWN", getValue().getAsString()).build();
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            player.openInventory(new SetTimeValueMenu(config, MessageHandler.getString(player, "configItemCooldownTitle"), configKey, parent).disableHours().getInventory());
        }
    },

    CHANGE_GOAL("currentGoal", 0) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            Goal goal = gameDispatcher.getHandler(GoalHandler.class).getActiveGoal();
            return new ItemBuilder(goal.getClass() == TimeGoal.class ? Material.COMPASS : Material.GRASS_BLOCK).translatable(player, "CHANGE_GOAL", goal.getDisplayName(), goal.getScoreboardGoalValue(player, true)).build();
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
            player.openInventory(gameDispatcher.getHandler(GoalHandler.class).getActiveGoal().getConfigMenu(player, parent).getInventory());
        }
    },

    CHANGE_FIRST_DROP_DELAY("firstItemDropDelay", 5) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.REPEATER).translatable(player, "CHANGE_FIRST_DROP_DELAY", TimeConverter.getTimeString(getValue().getAsInt())).build();
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            player.openInventory(new SetTimeValueMenu(config, MessageHandler.getString(player, "configFirstItemDropTitle"), configKey, parent).disableHours().setDisplayStack(() -> getDisplayItem(player)).getInventory());
        }
    },

    PLAY_TIME("playTime", 900) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.CLOCK).translatable(player, "PLAY_TIME", TimeConverter.getTimeString(getValue().getAsInt())).build();
        }
    },

    BLOCKS_TO_PLACE("blockToGoal", 100) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.GRASS_BLOCK).translatable(player, "BLOCKS_TO_PLACE", getValue().getAsString()).build();
        }
    },

    EXCLUDED_ITEMS("excludedItems", new String[]{"LIGHT"}) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            return new ItemBuilder(Material.BARRIER).translatable(player, "EXCLUDED_ITEMS").build();
        }

        @Override
        public void openConfigMenu(Player player, @org.jetbrains.annotations.Nullable Menu parent) {
            player.openInventory(new SaveItemMenu(config, configKey, MessageHandler.getString(player, "configExcludedItemsTitle"), parent).getInventory());
        }
    },

    BLOCK_DROP("doBlockDrop", false) {
        @Override
        public ItemStack getDisplayItem(Player player) {
            boolean val = getValue().getAsBoolean();
            return new ItemBuilder(val ? Material.STONE : Material.COBBLESTONE).translatable(player, "BLOCK_DROP", getStatusString(player)).build();
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

    protected String getStatusString(Player player) {
        boolean b = getValue().getAsBoolean();
        return MessageHandler.getString(player, "EN-/DISABLED").split("//")[b ? 0 : 1];
    }
}
