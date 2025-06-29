package de.gamdude.randomizer;

import de.gamdude.randomizer.game.handler.GameDispatcher;
import de.gamdude.randomizer.commands.GameCommand;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.game.options.Option;
import de.gamdude.randomizer.listener.*;
import de.gamdude.randomizer.world.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Locale;
import java.util.Objects;

public final class Randomizer extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        Locale.setDefault(Locale.US);
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
        pluginManager.registerEvents(new CommandPreProcessListener(), this);

        File bukkitFile = new File("bukkit.yml");
        if ( bukkitFile.exists() ) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(bukkitFile);
            if ( !yamlConfiguration.contains("worlds.world.generator") ) {
                getLogger().severe("World generator entry missing in bukkit.yml.");
                yamlConfiguration.set("worlds.world.generator", "Randomizer");
                try {
                    yamlConfiguration.save(bukkitFile);
                    getLogger().severe("Added 'worlds.world.generator: Randomizer' automatically.");
                    getLogger().severe("Please RESTART the server to apply the new configuration!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }
        }

        Objects.requireNonNull(getCommand("game")).setExecutor(new GameCommand(gameDispatcher));
    }

    @Override
    public void onDisable() {
        if ( !Option.DELETE_WORLD.getValue().getAsBoolean() )
            return;
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
