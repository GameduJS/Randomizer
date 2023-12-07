package de.gamdude.randomizer.ui.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    protected static final MiniMessage miniMessage = MiniMessage.miniMessage();
    protected Inventory inventory;

    public Menu(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(this, type, miniMessage.deserialize(title));
    }

    public Menu(int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, miniMessage.deserialize(title));
    }

    public abstract boolean onClick(Player player, int slot, ClickType type);

    public abstract void onOpen(Player player);

    public abstract void onClose(Player player);

    protected void fill(ItemStack item) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null ) continue;
            inventory.setItem(i, item);
        }
    }
    
    /**
     * @param row >> value of 1 - 6
     * @param itemStack
     */
    protected void fill(int row, ItemStack itemStack) {
    	/** Is only working on normal inventories **/
    	if(!this.inventory.getType().equals(InventoryType.CHEST)) return;
    	/** Cannot set items to slot greather than inventory size **/
    	if(row * 9 > this.inventory.getSize()) return;
    	
    	/** Fill in items into row **/
    	for(int i = (row - 1) * 9; i < row * 9; i++) {
    		if(inventory.getItem(i) != null) continue;
    		inventory.setItem(i, itemStack);
    	}
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}