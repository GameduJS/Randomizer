package de.gamdude.randomizer.ui.base;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public abstract class ChildMenu extends Menu {

    protected final Menu parentMenu;

    public ChildMenu(Menu parentMenu, InventoryType type, String title) {
        super(type, title);
        this.parentMenu = parentMenu;
    }

    public ChildMenu(Menu parentMenu, int size, String title) {
        super(size, title);
        this.parentMenu = parentMenu;
    }

    @Override
    public void onClose(Player player) {
        if(parentMenu != null)
            player.openInventory(parentMenu.getInventory());
    }
}
