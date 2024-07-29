package de.gamdude.randomizer;

import de.gamdude.randomizer.game.handler.GameDispatcher;
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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class Randomizer extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
         Config config = new Config(this, "randomizer");
         GameDispatcher gameDispatcher = new GameDispatcher(this, config);

        pluginManager.registerEvents(new PlayerConnectionListener(gameDispatcher), this);
        pluginManager.registerEvents(new PlayerMoveListener(gameDispatcher), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new BlockInteractListener(gameDispatcher), this);
        pluginManager.registerEvents(new MenuListener(this), this);
        pluginManager.registerEvents(new PlayerListener(gameDispatcher), this);
        pluginManager.registerEvents(new WorldLoadListener(), this);
        pluginManager.registerEvents(new ItemInteractListener(gameDispatcher), this);
        pluginManager.registerEvents(new CommandPreProcessListener(gameDispatcher), this);

        Objects.requireNonNull(getCommand("game")).setExecutor(new GameCommand(gameDispatcher));
    }

    @Override
    public void onDisable() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDirectory(Objects.requireNonNull(Bukkit.getWorld("world")).getWorldFolder()) ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public @NotNull ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new VoidWorldGenerator();
    }
}
