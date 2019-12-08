package io.prodity.commons.spigot.inventory;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum InventoryUtil {

    ;

    /**
     * Adds the specified {@link ItemStack}s to the specified {@link Player}'s inventory.<br>
     * If any items were not added, they will be dropped at the player's feet
     *
     * @param player the {@link Player}
     * @param itemStacks the {@link ItemStack}s
     * @return true if all items were added to the {@link Player}s inventory, false if some were dropped.
     */
    public static boolean addToPlayer(Player player, ItemStack... itemStacks) {
        final Collection<ItemStack> failed = player.getInventory().addItem(itemStacks).values();
        if (failed.isEmpty()) {
            return true;
        }

        failed.forEach((item) -> player.getWorld().dropItem(player.getLocation(), item));
        return false;
    }

}