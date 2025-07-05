package de.gamdude.randomizer.world;

import org.bukkit.Location;
import java.util.ArrayList;
import java.util.List;

public class Platform {

    private final Location platformLocation;
    private boolean enabled;

    private List<Location> blocksList;
    private Location lastBlockBuilt;
    private int blocksBuilt;

    public Platform(Location location) {
        this.platformLocation = location;
        this.lastBlockBuilt = platformLocation.toBlockLocation().add(0, -1, 0); // platformloc is block above bedrock for spawning purposes
        this.blocksList = new ArrayList<>();
        this.enabled = true; //default
    }

    public void addBlock(Location blockLocation) {
        blocksList.add(blockLocation);
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

    public List<Location> getBlocksList() {
        return blocksList;
    }

    public boolean locationWithIn(Location location) {
        return location.getChunk().getX() == platformLocation.getChunk().getX();
    }

    public void increaseBlocksBuilt() {
        this.blocksBuilt+=1 ;
    }

    public int getBlocksBuilt() {
        return blocksBuilt;
    }
}
