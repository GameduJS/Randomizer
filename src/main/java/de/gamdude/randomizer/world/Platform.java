package de.gamdude.randomizer.world;

import org.bukkit.Location;

public class Platform {

    private final Location platformLocation;
    private boolean enabled;

    private Location lastBlockBuilt;
    private int blocksBuilt;

    public Platform(Location location) {
        this.platformLocation = location;
        this.lastBlockBuilt = platformLocation.toBlockLocation();
        this.enabled = true; //default
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
