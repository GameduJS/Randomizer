package de.gamdude.randomizer.base;

import de.gamdude.randomizer.Randomizer;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.ui.visuals.RandomizerScoreboard;
import de.gamdude.randomizer.world.PlatformLoader;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class GameDispatcher {

    private final Randomizer plugin;
    private final Config config;

    private final ItemDropDeployer itemDropDeployer;
    private final PlatformLoader platformLoader;
    private final RandomizerScoreboard randomizerScoreboard;
    private final PlayerProgressTracker playerProgressHandle;
    private final LeaderboardHandler leaderboardHandler;

    private long taskID;
    private int seconds;
    private int timeToPlay;
    /**
     * 0 - not started
     * 1 - started / running
     * 2 - paused
     * 3 - end
     */
    private int state;

    public GameDispatcher(Randomizer plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.itemDropDeployer = new ItemDropDeployer(this);
        this.platformLoader = new PlatformLoader();
        this.playerProgressHandle = new PlayerProgressTracker(this);
        this.leaderboardHandler = new LeaderboardHandler(this);
        this.randomizerScoreboard = new RandomizerScoreboard(this);

        startScheduler();
    }

    public void startScheduler() {
        if(taskID != 0)
            return;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            // playing
            if(state == 1) {
                if(seconds == timeToPlay)
                    stopGame();
                itemDropDeployer.dropQueue(seconds);
                Bukkit.getOnlinePlayers().forEach(randomizerScoreboard::updateScoreboard);

                seconds++;
            }
        }, 0, 20);
    }

    public void loadConfig() {
        itemDropDeployer.loadConfig();
        randomizerScoreboard.loadConfig();
        this.timeToPlay = config.getProperty("playTime").getAsInt();
    }

    public void startGame() {
        if(state > 0)
            return;
        loadConfig();

        boolean spawnWithDefaults = config.getProperty("spawnWithDefaults").getAsBoolean();

        Bukkit.getOnlinePlayers().forEach(player-> {
            leaderboardHandler.updateLeaderboard(player.getUniqueId());

            if(spawnWithDefaults) {
                player.getInventory().setItem(0, new ItemStack(Material.DIRT));
                player.getInventory().setItem(1, new ItemStack(Material.OAK_SAPLING));
            }
        });

        this.state = 1;
    }

    public void pause() {
        if(state == 2)
            state = 1;
        else if(state == 1)
            state = 2;
    }

    public void stopGame() {
        state = 3;
        UUID topPlayerID = getLeaderboardHandler().getTopPlayers().getPlayerList().get(0);
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<color:#f878ff>Randomizer <dark_gray>| <yellow>" + Bukkit.getOfflinePlayer(topPlayerID).getName()
                 + " <gray>(<yellow>"+ playerProgressHandle.getBlocksBuilt(topPlayerID) + "<gray>) has won the race!"));

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<color:#f878ff>Randomizer <dark_gray>| <red>Server will shutdown in 10 seconds!"));
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

    public PlayerProgressTracker getPlayerProgressHandle() {
        return playerProgressHandle;
    }

    public PlatformLoader getPlatformLoader() {
        return platformLoader;
    }

    public RandomizerScoreboard getRandomizerScoreboard() {
        return randomizerScoreboard;
    }

    public LeaderboardHandler getLeaderboardHandler() {
        return leaderboardHandler;
    }
}
