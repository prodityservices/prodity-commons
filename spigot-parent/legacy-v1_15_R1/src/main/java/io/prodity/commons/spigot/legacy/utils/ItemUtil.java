package io.prodity.commons.spigot.legacy.utils;

import io.prodity.commons.pair.ImmutablePair;
import io.prodity.commons.pair.Pair;
import io.prodity.commons.spigot.legacy.placeholder.PlaceholderHelper;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 11/13/2017 <p> This utility class is being used for all ItemStack related things.
 */
@UtilityClass
@Deprecated
public class ItemUtil {

    /**
     * It will parse the {@link ConfigurationSection} which has been passed to return an {@link ItemStack} allowing us
     * to load items for menus and such.
     */
    public static ItemStack createItemStackFromConfigurationSection(
        @NonNull ConfigurationSection configurationSection) {
        Material material = Material.valueOf(configurationSection.getString("material"));
        ItemStack itemStack = new ItemStack(material);
        int amount = configurationSection.get("amount") != null ? configurationSection.getInt("amount") : -1;
        if (amount != -1) {
            itemStack.setAmount(amount);
        }

        byte durability = configurationSection.get("durability") != null ? Byte
            .parseByte(configurationSection.getInt("durability") + "") : -1;
        if (durability != -1) {
            itemStack.setDurability(durability);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = configurationSection.getString("name");
        if (displayName != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }

        List<String> itemLore = configurationSection.getStringList("lore");
        if (itemLore != null) {
            List<String> newLore = new ArrayList<>();
            for (String lore : itemLore) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            itemMeta.setLore(newLore);
        }

        if (itemMeta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            String owner = configurationSection.getString("owner");
            if (owner != null) {
                skullMeta.setOwner(owner);
            }
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * This will take an {@link ItemStack} and go through all of its text based components, such as display name and
     * lore and use the {@link PlaceholderHelper} to set all the placeholders which are required.
     */
    public static ItemStack filterPlaceholders(@NonNull Player player, @NonNull ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(PlaceholderHelper.setPlaceholders(player, itemMeta.getDisplayName()));
            }
            if (itemMeta.hasLore()) {
                List<String> newLore = PlaceholderHelper.setPlaceholders(player, itemMeta.getLore());
                itemMeta.setLore(newLore);
            }

            if (itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                if (skullMeta.hasOwner()) {
                    skullMeta.setOwner(PlaceholderHelper.setPlaceholders(player, skullMeta.getOwner()));
                }
            }
        }

        return itemStack;
    }

    public static ItemStack filterPlaceholderWithConsumer(@NonNull ItemStack itemStack, Player player,
        @NonNull ItemCallback callback) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(callback.call(player, ImmutablePair.with("name", itemMeta.getDisplayName())));
        }

        if (itemMeta.hasLore()) {
            List<String> newLore = new ArrayList<>();
            for (String lore : itemMeta.getLore()) {
                newLore.add(callback.call(player, ImmutablePair.with("lore", lore)));
            }

            itemMeta.setLore(newLore);
        }

        if (itemMeta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            if (skullMeta.hasOwner()) {
                skullMeta.setOwner(callback.call(player, ImmutablePair.with("owner", skullMeta.getOwner())));
            }
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public interface ItemCallback {

        String call(Player player, Pair<String, String> textPair);

    }

}
