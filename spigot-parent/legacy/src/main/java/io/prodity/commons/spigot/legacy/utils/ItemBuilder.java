package io.prodity.commons.spigot.legacy.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Deprecated
public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(Material mat) {
        this.item = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder clearName() {
        Material falseMaterial = this.item.getType();
        this.item = new ItemStack(falseMaterial);
        return this;
    }

    public ItemBuilder name(String name) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(StringUtils.color(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name) {
        final ItemMeta meta = this.item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(StringUtils.color(name));
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> name) {
        final ItemMeta meta = this.item.getItemMeta();
        List<String> temp = new ArrayList<>();
        for (String line : name) {
            temp.add(StringUtils.color(line));
        }
        meta.setLore(temp);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(new ArrayList<>());
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchantBook(Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder data(int data) {
        this.item.setDurability((short) data);
        return this;
    }

    public ItemBuilder head(OfflinePlayer player) {
        this.item.setDurability((short) 3);

        SkullMeta meta = (SkullMeta) this.item.getItemMeta();
        meta.setOwner(player.getName());
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, final int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }

}
