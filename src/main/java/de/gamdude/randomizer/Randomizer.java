package de.gamdude.randomizer;

import de.gamdude.randomizer.base.GameDispatcher;
import de.gamdude.randomizer.commands.GameCommand;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.listener.*;
import de.gamdude.randomizer.world.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public final class Randomizer extends JavaPlugin {

    public final Config config = new Config(this, "randomizer");
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        GameDispatcher gameDispatcher = new GameDispatcher(this);
        pluginManager.registerEvents(new PlayerConnectionListener(gameDispatcher), this);
        pluginManager.registerEvents(new PlayerMoveListener(gameDispatcher), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new BlockInteractListener(gameDispatcher), this);
        pluginManager.registerEvents(new MenuListener(this), this);
        pluginManager.registerEvents(new PlayerListener(gameDispatcher), this);

        getCommand("game").setExecutor(new GameCommand(gameDispatcher));
    }
    @Override
    public void onDisable() {
        config.savePropertiesToFile();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDirectory(Objects.requireNonNull(Bukkit.getWorld("world")).getWorldFolder()) ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new VoidWorldGenerator();
    }

    public Config getConfiguration() {
        return config;
    }
}
