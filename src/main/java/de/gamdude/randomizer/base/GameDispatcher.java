package de.gamdude.randomizer.base;

import de.gamdude.randomizer.Randomizer;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.RandomizerScoreboard;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Bukkit;


public class GameDispatcher {

    private final Randomizer plugin;
    private final Config config;

    private final ItemDropDeployer itemDropDeployer;
    private final PlatformLoader platformLoader;
    private final RandomizerScoreboard randomizerScoreboard;
    private final PlayerProgressTracker playerProgressHandle;

    private long taskID;
    private int seconds;
    /**
     * 0 - not started
     * 1 - started / running
     * 2 - paused
     */
    private int state;

    public GameDispatcher(Randomizer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.itemDropDeployer = new ItemDropDeployer(this);
        this.platformLoader = new PlatformLoader();
        this.playerProgressHandle = new PlayerProgressTracker(this);
        this.randomizerScoreboard = new RandomizerScoreboard(this);

        startScheduler();
    }

    public void startScheduler() {
        if(taskID != 0)
            return;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            // playing
            if(state == 1) {
                itemDropDeployer.dropQueue(seconds);
            }

            Bukkit.getOnlinePlayers().forEach(randomizerScoreboard::updateScoreboard);
            seconds++;
        }, 0, 20);
    }

    public void loadConfig() {
        // load config values
    }

    public void startGame() {
        if(state != 0)
            return;
        this.state = 1;
    }

    public int getState() {
        return state;
    }

    public Config getConfig() {
        return config;
    }

    public PlayerProgressTracker getPlayerProgressHandle() {
        return playerProgressHandle;
    }

    public ItemDropDeployer getItemDropDeployer() {
        return itemDropDeployer;
    }

    public PlatformLoader getPlatformLoader() {
        return platformLoader;
    }

    public RandomizerScoreboard getRandomizerScoreboard() {
        return randomizerScoreboard;
    }
}
