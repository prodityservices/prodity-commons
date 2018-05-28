package io.prodity.commons.spigot.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Utilities for serializing & deserializing {@link ItemStack}s.
 */
public enum ItemSerializer {

    ;

    /**
     * Serializes the specified {@link ItemStack} array to base64.
     *
     * @param itemStacks the array
     * @return the serialized array, or an empty string if the specified array is empty
     */
    public static String itemStackArrayToBase64(ItemStack[] itemStacks) throws IOException {
        Preconditions.checkNotNull(itemStacks, "itemStacks");

        if (itemStacks.length == 0) {
            return "";
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(outputStream)
        ) {
            bukkitOutput.writeInt(itemStacks.length);

            for (ItemStack itemStack : itemStacks) {
                bukkitOutput.writeObject(itemStack);
            }

            final byte[] outputByteArray = outputStream.toByteArray();
            return Base64Coder.encodeLines(outputByteArray);
        }
    }

    /**
     * Serializes the specified {@link ItemStack} {@link List} to base64.
     *
     * @param itemStacks the list
     * @return the serialized list, or an empty string if the specified list is empty
     */
    public static String itemStackListToBase64(List<ItemStack> itemStacks) throws IOException {
        Preconditions.checkNotNull(itemStacks, "itemStacks");
        if (itemStacks.isEmpty()) {
            return "";
        }
        return ItemSerializer.itemStackArrayToBase64(itemStacks.toArray(new ItemStack[]{}));
    }

    /**
     * Deserializes the specified base64 {@link String} to an {@link ItemStack} array.
     *
     * @param itemListString the base64 {@link String}
     * @return the {@link ItemStack} array, empty if the specified string is empty or null
     */
    public static ItemStack[] itemStackArrayFromBase64(String itemListString) throws IOException, ClassNotFoundException {
        if (itemListString == null || itemListString.isEmpty()) {
            return new ItemStack[0];
        }

        final byte[] data = Base64Coder.decodeLines(itemListString);

        try (
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream bukkitInput = new BukkitObjectInputStream(inputStream)
        ) {
            final int length = bukkitInput.readInt();
            final ItemStack[] itemStacks = new ItemStack[length];

            for (int i = 0; i < length; i++) {
                final ItemStack itemStack = (ItemStack) bukkitInput.readObject();
                itemStacks[i] = itemStack;
            }

            return itemStacks;
        }
    }

    /**
     * Deserializes the specified base64 {@link String} to an {@link ItemStack} {@link List}.
     *
     * @param itemListString the base64 {@link String}
     * @return the {@link ItemStack} {@link List}, empty if the specified string is empty or null
     */
    public static List<ItemStack> itemStackListFromBase64(String itemListString) throws IOException, ClassNotFoundException {
        if (itemListString == null || itemListString.isEmpty()) {
            return Lists.newArrayList();
        }
        final ItemStack[] deserialized = ItemSerializer.itemStackArrayFromBase64(itemListString);
        return Arrays.asList(deserialized);
    }

    /**
     * Compresses the specified {@link ItemStack} array so that similar items are stacked with each other.
     *
     * @param itemStacks the array
     * @return the compressed array
     */
    public static ItemStack[] compressItemStackArray(ItemStack[] itemStacks) {
        final ItemStack[] compressed = new ItemStack[itemStacks.length];

        outerLoop:
        for (int index = 0; index < itemStacks.length; index++) {
            final ItemStack notCompressed = itemStacks[index] == null ? null : itemStacks[index].clone();

            if (notCompressed == null || notCompressed.getType() == Material.AIR) {
                continue;
            }

            for (int compressedIndex = 0; compressedIndex < compressed.length; compressedIndex++) {
                final ItemStack compressedItemStack = compressed[compressedIndex];
                if (compressedItemStack == null) {
                    compressed[compressedIndex] = notCompressed;
                    continue outerLoop;
                }

                if (!compressedItemStack.isSimilar(notCompressed)) {
                    continue;
                }

                final int notCompressedAmount = notCompressed.getAmount();
                final int compressedAmount = compressedItemStack.getAmount();
                final int maxStackSize = compressedItemStack.getType().getMaxStackSize();

                final int amountToAdd = Math.min(notCompressedAmount, maxStackSize - compressedAmount);

                compressedItemStack.setAmount(compressedAmount + amountToAdd);
                if (amountToAdd == notCompressedAmount) {
                    continue outerLoop;
                } else {
                    notCompressed.setAmount(notCompressedAmount - amountToAdd);
                }
            }

        }

        return compressed;
    }

    /**
     * Compresses the specified {@link ItemStack} list so that similar items are stacked with each other.
     *
     * @param itemStacks the {@link List}
     * @return a newly created {@link List} of the compressed {@link ItemStack}s
     */
    public static List<ItemStack> compressItemStackList(List<ItemStack> itemStacks) {
        final ItemStack[] itemStackArray = itemStacks.toArray(new ItemStack[]{});
        final ItemStack[] compressed = ItemSerializer.compressItemStackArray(itemStackArray);
        return Arrays.asList(compressed);
    }

}