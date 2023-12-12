package de.gamdude.randomizer.base;

import com.google.gson.JsonElement;
import de.gamdude.randomizer.base.structure.Platform;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ItemDropDeployer {

    private final GameDispatcher gameDispatcher;

    private final List<Material> ignoredMaterials;
    private Material[] MATERIALS;
    private final Random RANDOM = ThreadLocalRandom.current();

    private int dropDelay;
    private int dropCooldown;

    public ItemDropDeployer(GameDispatcher gameDispatcher) {
        this.gameDispatcher = gameDispatcher;
        this.ignoredMaterials = new ArrayList<>();
    }

    public void loadConfig() {
        for (JsonElement jsonElement : gameDispatcher.getConfig().getProperty("excludedItems").getAsJsonArray())
            ignoredMaterials.add(Material.valueOf(jsonElement.getAsString()));

        dropDelay = gameDispatcher.getConfig().getProperty("firstItemDropDelay").getAsInt();
        dropCooldown = gameDispatcher.getConfig().getProperty("itemCooldown").getAsInt();

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
            gameDispatcher.getPlatformLoader().getPlatforms().stream().map(Map.Entry::getValue).filter(Platform::isEnabled).map(Platform::getPlatformLocation).forEach(this::dropItem);
    }

}
