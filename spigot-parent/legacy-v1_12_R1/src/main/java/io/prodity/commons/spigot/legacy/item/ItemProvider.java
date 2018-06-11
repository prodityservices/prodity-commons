package io.prodity.commons.spigot.legacy.item;

import io.prodity.commons.spigot.legacy.version.VersionProvider;
import org.bukkit.inventory.ItemStack;

public interface ItemProvider extends VersionProvider {

    ItemStack toCraftItemStack(ItemStack itemStack);

    ItemStack toBukkitItemStack(ItemStack itemStack);

}
