package de.gamdude.randomizer.world;

import de.gamdude.randomizer.base.structure.Platform;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class PlatformLoader {

    private final Map<UUID, Platform> playerPlatformLocationMap = new HashMap<>();


    public Platform createPlatform(Player player, int playerCount) {
        if(playerPlatformLocationMap.containsKey(player.getUniqueId()))
            return getPlatform(player.getUniqueId());
        World world = Objects.requireNonNull(Bukkit.getWorld("world"), "Could not load 'world'");

        int xCoordinate = 7 + 16 * (playerCount - 1);
        world.getBlockAt(xCoordinate, 63, 0).setType(Material.BEDROCK);

        for(int offX = -1; offX <= 1; ++offX) {
            for(int offY = -1; offY <= 1; ++offY) {
                world.getBlockAt(xCoordinate + offX, 65 + offY, -1).setType(Material.BARRIER);
            }
        }

        Location location =  new Location(world, xCoordinate, 64, 0, 0, 0).toCenterLocation();
        Platform platform = new Platform(player.getUniqueId(), location);
        this.playerPlatformLocationMap.put(player.getUniqueId(), platform);

        return platform;
    }

    public Platform getPlatform(UUID uuid) {
        return this.playerPlatformLocationMap.get(uuid);
    }

    public Set<Map.Entry<UUID, Platform>> getPlatforms() {
        return this.playerPlatformLocationMap.entrySet();
    }

}
