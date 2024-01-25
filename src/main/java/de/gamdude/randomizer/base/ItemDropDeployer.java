package de.gamdude.randomizer.base;

import com.google.gson.JsonElement;
import de.gamdude.randomizer.world.Platform;
import de.gamdude.randomizer.config.Config;
import de.gamdude.randomizer.world.PlatformLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ItemDropDeployer implements Handler {

    private final PlatformLoader platformLoader;

    private final List<Material> ignoredMaterials;
    private Material[] MATERIALS;
    private final Random RANDOM = ThreadLocalRandom.current();

    private int dropDelay;
    private int dropCooldown;

    public ItemDropDeployer(GameDispatcher gameDispatcher) {
        this.platformLoader = gameDispatcher.getHandler(PlatformLoader.class);
        this.ignoredMaterials = new ArrayList<>();
    }

    @Override
    public void loadConfig(Config config) {
        for (JsonElement jsonElement : config.getProperty("excludedItems").getAsJsonArray())
            ignoredMaterials.add(Material.valueOf(jsonElement.getAsString()));

        dropDelay = config.getProperty("firstItemDropDelay").getAsInt();
        dropCooldown = config.getProperty("itemCooldown").getAsInt();

        MATERIALS = Arrays.stream(Material.values()).filter(Predicate.not(Material::isLegacy)).filter(Material::isItem).filter(Predicate.not(ignoredMaterials::contains)).toArray(Material[]::new);
    }

    public void dropItem(Location location) {
        Material itemMaterial = MATERIALS[RANDOM.nextInt(MATERIALS.length)];
        Item droppedItem = location.getWorld().dropItem(location, new ItemStack(itemMaterial, 1));
        droppedItem.setVelocity(new Vector(0, 0, 0));
    }

    public void dropQueue(long seconds) {
        if (seconds - dropDelay < 0) // Wait dropDelay
            return;
        if ((seconds - dropDelay) % dropCooldown == 0)  // Perform action every dropCooldown seconds
            platformLoader.getPlatforms().stream().map(Map.Entry::getValue).filter(Platform::isEnabled).map(Platform::getPlatformLocation).forEach(this::dropItem);
    }

}
