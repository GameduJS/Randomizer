package de.gamdude.randomizer.ui.menu;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected static final MiniMessage miniMessage = MiniMessage.miniMessage();
    protected Inventory inventory;
    private final Menu parent;

    public Menu(int size, String title) {
        this(null, size, title);
    }

    public Menu(Menu parent, int size, String title) {
        this.parent = parent;
        this.inventory = Bukkit.createInventory(this, size, miniMessage.deserialize(title));
    }

    public abstract boolean onClick(Player player, int slot, ClickType type);

    public abstract void onOpen(Player player);

    public void onClose(Player player) {
        if(parent != null)
            player.openInventory(parent.getInventory());
    }

    protected void fill(ItemStack item) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null ) continue;
            inventory.setItem(i, item);
        }
    }
    
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public boolean allowPlayerInventoryInteraction() {
        return false;
    }
}