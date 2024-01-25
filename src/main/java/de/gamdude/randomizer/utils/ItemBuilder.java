package de.gamdude.randomizer.utils;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private final MiniMessage miniMessage = MiniMessage.builder()
			.postProcessor(component -> component.decoration(TextDecoration.ITALIC, false)).build();

	private Component displayName;
	private final ItemStack item;
	private final ItemMeta meta;
	private final List<Component> lore;


	public ItemBuilder(Material material) {
		this.displayName = Component.text("");
		this.item = new ItemStack(material);
		this.meta = item.getItemMeta();
		this.lore = new ArrayList<>();
	}

	public ItemBuilder setDisplayName(String displayName) {
		this.displayName = miniMessage.deserialize(displayName);
		return this;
	}

	public ItemBuilder setLore(String lore) {
		this.lore.add(miniMessage.deserialize(lore));
		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag flag) {
		this.meta.addItemFlags(flag);
		return this;
	}

	public ItemStack build() {
		this.meta.displayName(this.displayName);
		this.meta.lore(lore);
		this.item.setItemMeta(this.meta);

		return this.item;
	}

}
