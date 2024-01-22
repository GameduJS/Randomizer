package de.gamdude.randomizer.listener;

import de.gamdude.randomizer.base.GameDispatcher;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    private final GameDispatcher gameDispatcher;

    public WorldLoadListener(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        world.setTime(6000L);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, gameDispatcher.getConfig().getProperty("doDayNightCycle").getAsBoolean());
    }

}
