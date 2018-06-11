package io.prodity.commons.spigot.legacy.nbt;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import io.prodity.commons.spigot.legacy.item.ItemUtil;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class NbtHelper {

    public static Optional<NbtCompound> fromItemOptional(ItemStack itemStack) {
        return NbtFactory.fromItemOptional(itemStack).map(NbtFactory::asCompound);
    }

    public static NbtCompound fromItem(ItemStack itemStack) {
        return NbtFactory.asCompound(NbtFactory.fromItemTag(itemStack));
    }

    public static boolean itemHasNbtKey(ItemStack itemStack, String key) {
        itemStack = ItemUtil.toCraftItem(itemStack);

        final NbtCompound compound = NbtHelper.fromItem(itemStack);
        return compound.containsKey(key);
    }

    public static <T> ItemStack modifyItemCompound(ItemStack itemStack, Consumer<NbtCompound> compoundModifier) {
        itemStack = ItemUtil.toCraftItem(itemStack);

        final NbtCompound compound = NbtHelper.fromItem(itemStack);
        compoundModifier.accept(compound);
        NbtFactory.setItemTag(itemStack, compound);

        return ItemUtil.toBukkitItem(itemStack);
    }

}