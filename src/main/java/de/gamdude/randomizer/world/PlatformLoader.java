package de.gamdude.randomizer.world;

import de.gamdude.randomizer.game.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.*;

public class PlatformLoader implements Handler {

    private final Map<UUID, Platform> playerPlatformLocationMap = new HashMap<>();

    public Platform createPlatform(UUID uuid, int playerCount) {
        if(playerPlatformLocationMap.containsKey(uuid))
            return getPlatform(uuid);
        World world = Objects.requireNonNull(Bukkit.getWorld("world"), "Could not load 'world'");

        int xCoordinate = 7 + 16 * (playerCount - 1);
        world.getBlockAt(xCoordinate, 63, 0).setType(Material.BEDROCK);

        for(int offX = -1; offX <= 1; ++offX) {
            for(int offY = -1; offY <= 1; ++offY) {
                world.getBlockAt(xCoordinate + offX, 65 + offY, -1).setType(Material.BARRIER);
            }
        }

        Location location =  new Location(world, xCoordinate, 64, 0, 0, 0).toCenterLocation();
        Platform platform = new Platform(location);
        this.playerPlatformLocationMap.put(uuid, platform);

        return platform;
    }

    public Platform getPlatform(UUID uuid) {
        return this.playerPlatformLocationMap.get(uuid);
    }

    public Set<Map.Entry<UUID, Platform>> getPlatforms() {
        return this.playerPlatformLocationMap.entrySet();
    }

}
