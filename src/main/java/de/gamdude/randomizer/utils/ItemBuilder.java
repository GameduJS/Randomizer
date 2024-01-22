package de.gamdude.randomizer.utils;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemBuilder {

	private final MiniMessage miniMessage = MiniMessage.builder()
			.postProcessor(component -> component.decoration(TextDecoration.ITALIC, false)).build();

	private Material material;
	private Component displayName;
	private final ItemStack item;
	private final ItemMeta meta;
	private List<Component> lore;
	private int amount;

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(Material material, int amount) {
		this.material = material;
		this.displayName = Component.text("");
		this.amount = amount;
		this.item = new ItemStack(material, amount);
		this.meta = item.getItemMeta();
		this.lore = new ArrayList<Component>();
	}

	public ItemBuilder setMaterial(Material material) {
		this.material = material;
		return this;
	}

	public ItemBuilder setDisplayName(String displayName) {
		this.displayName = miniMessage.deserialize(displayName);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		this.meta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemBuilder addHiddenEnchantment() {
		this.meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	public ItemBuilder setLore(String lore) {
		this.lore.add(miniMessage.deserialize(lore));
		return this;
	}

	public ItemBuilder setDurability(int dura) {
		((Damageable) meta).setDamage(dura);
		return this;
	}

	public ItemBuilder amount(int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemBuilder unbreakable(boolean b) {
		this.meta.setUnbreakable(true);
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
