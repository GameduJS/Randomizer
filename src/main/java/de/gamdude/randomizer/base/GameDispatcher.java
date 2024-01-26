package de.gamdude.randomizer.base;

import de.gamdude.randomizer.Randomizer;
import de.gamdude.randomizer.game.goals.GoalHandler;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.ui.visuals.RandomizerScoreboard;
import de.gamdude.randomizer.utils.MessageHandler;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GameDispatcher {

    private final Randomizer plugin;
    private final Config config;

    private final Map<Class<? extends Handler>, Handler> handlerMap;
    private final RandomizerScoreboard randomizerScoreboard;

    private long taskID;
    private int seconds;
    /**
     * 0 - not started
     * 1 - started / running
     * 2 - paused
     * 3 - end
     */
    private int state;

    public GameDispatcher(Randomizer plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        this.handlerMap = new HashMap<>();
        this.handlerMap.put(PlatformLoader.class, new PlatformLoader());
        this.handlerMap.put(ItemDropDeployer.class, new ItemDropDeployer(this));
        this.handlerMap.put(PlayerProgressTracker.class, new PlayerProgressTracker(this));
        this.handlerMap.put(LeaderboardHandler.class, new LeaderboardHandler(this));
        this.handlerMap.put(GoalHandler.class, new GoalHandler(this));

        this.randomizerScoreboard = new RandomizerScoreboard(this);

        Option.injectGameDispatcher(this);
        startScheduler();
    }

    @SuppressWarnings("unchecked")
    public <T extends Handler> T getHandler(Class<T> clazz) {
        return (T) this.handlerMap.get(clazz);
    }

    /**
     * Called at the start of the game to load the configs
     */
    private void loadConfig() {
        this.handlerMap.values().forEach(handler -> handler.loadConfig(config));
    }

    /**
     * Starts the scheduler for the game loop.
     */
    private void startScheduler() {
        if(taskID != 0)
            return;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            // playing
            if(state == 1) {
                if(getHandler(GoalHandler.class).isFinished())
                    stopGame();
                getHandler(ItemDropDeployer.class).dropQueue(seconds);
                Bukkit.getOnlinePlayers().forEach(player -> randomizerScoreboard.updateScoreboard());
                seconds++;
            }
        }, 0, 20);
    }

    public void startGame() {
        if(state > 0)
            return;
        this.state = 1;
        loadConfig();

        boolean spawnWithDefaults = Option.ENABLE_START_ITEMS.getValue().getAsBoolean();
        Bukkit.getOnlinePlayers().forEach(player-> {
            getHandler(LeaderboardHandler.class).updateLeaderboard(player.getUniqueId());

            if(spawnWithDefaults) {
                player.getInventory().setItem(0, new ItemStack(Material.DIRT));
                player.getInventory().setItem(1, new ItemStack(Material.OAK_SAPLING));
            }
        });
    }

    public void pause() {
        switch (state) {
            case 1 -> state = 2;
            case 2 -> state = 1;
        }
    }

    public void stopGame() {
        if(state == 0 || state == 3)
            return;
        state = 3;
        UUID topPlayerID = getHandler(LeaderboardHandler.class).getTopPlayers().getPlayerList().get(0);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            MessageHandler.sendMessage(onlinePlayer, "playerWon", Bukkit.getOfflinePlayer(topPlayerID).getName(), getHandler(PlayerProgressTracker.class).getBlocksBuilt(topPlayerID) + "");
            MessageHandler.sendMessage(onlinePlayer, "serverShutdown");
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Bukkit::shutdown, 200);
    }

    public int getSecondsPlayed() {
        return seconds;
    }

    public int getState() {
        return state;
    }

    public Config getConfig() {
        return config;
    }

    public RandomizerScoreboard getRandomizerScoreboard() {
        return randomizerScoreboard;
    }

}
