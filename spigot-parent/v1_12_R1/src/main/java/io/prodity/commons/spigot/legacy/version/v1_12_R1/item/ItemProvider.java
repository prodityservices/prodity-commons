package io.prodity.commons.spigot.legacy.version.v1_12_R1.item;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemProvider implements io.prodity.commons.spigot.legacy.item.ItemProvider {

	@Override
	public Object toCraftItemStack(ItemStack itemStack) {
		return CraftItemStack.asCraftCopy(itemStack);
	}

	@Override
	public ItemStack toBukkitItemStack(Object itemStack) {
		if (itemStack instanceof CraftItemStack) {
			return (CraftItemStack) itemStack;
		}

		return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) itemStack);
	}

}
