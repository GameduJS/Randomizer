package de.gamdude.randomizer.base.structure;

import org.bukkit.Location;

import java.util.UUID;

public class Platform {

    private final UUID playerID;
    private final Location platformLocation;
    private boolean enabled;

    private Location lastBlockBuilt;
    private int blocksBuilt;

    public Platform(UUID playerID, Location location) {
        this.playerID = playerID;
        this.platformLocation = location;
        this.lastBlockBuilt = platformLocation.toBlockLocation();
        this.enabled = true; //default
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Location getPlatformLocation() {
        return platformLocation;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLastBlockBuilt(Location lastBlockBuilt) {
        this.lastBlockBuilt = lastBlockBuilt;
    }

    public Location getLastBlockBuilt() {
        return lastBlockBuilt;
    }

    public void increaseBlocksBuilt() {
        this.blocksBuilt+=1 ;
    }

    public int getBlocksBuilt() {
        return blocksBuilt;
    }
}
