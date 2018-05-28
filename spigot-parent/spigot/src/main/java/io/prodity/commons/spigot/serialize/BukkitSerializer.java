package io.prodity.commons.spigot.serialize;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.array.ArrayUtil;
import io.prodity.commons.except.tryto.CheckedConsumer;
import io.prodity.commons.except.tryto.CheckedFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public enum BukkitSerializer {

    ;

    /**
     * Serializes the specified {@link Object} to base64 using a {@link BukkitObjectOutputStream}.
     *
     * @param object the object
     * @return the serialized object in base64
     */
    public static <T> String objectToBase64(T object) throws IOException {
        Preconditions.checkNotNull(object, "object");
        return BukkitSerializer.tryOutputStream((bukkitOutput) -> bukkitOutput.writeObject(object));
    }

    /**
     * Serializes the specified array to base64 using a {@link BukkitObjectOutputStream}.
     *
     * @param array the array
     * @return the serialized array in base64, or an empty string if the specified array is empty
     */
    public static <T> String arrayToBase64(T[] array) throws IOException {
        Preconditions.checkNotNull(array, "array");

        if (array.length == 0) {
            return "";
        }

        return BukkitSerializer.tryOutputStream((bukkitOutput) -> {
            bukkitOutput.writeInt(array.length);
            for (T object : array) {
                bukkitOutput.writeObject(object);
            }
        });
    }

    /**
     * Serializes the specified {@link Collection} to base64 using a {@link BukkitObjectOutputStream}.
     *
     * @param collection the collection
     * @return the serialized collection in base64, or an empty string if the specified collection is empty
     */
    public static <T> String collectionToBase64(Collection<? extends T> collection) throws IOException {
        Preconditions.checkNotNull(collection, "collection");
        if (collection.isEmpty()) {
            return "";
        }
        return BukkitSerializer.arrayToBase64(collection.toArray());
    }

    /**
     * Deserializes the specified base64 {@link String} to the provided type.
     *
     * @param type the {@link TypeToken} of the type to deserialize to
     * @param base64String the serialized object
     * @param <T> the type to deserialize to
     * @return the deserialized object, or null if the specified string is null or empty
     */
    @Nullable
    public static <T> T objectFromBase64(TypeToken<T> type, String base64String) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }

        return BukkitSerializer.tryInputStream(base64String, (input) -> {
            return (T) input.readObject();
        });
    }

    /**
     * Deserializes the specified base64 {@link String} to an array of the provided type.
     *
     * @param type the {@link TypeToken} of the array's type to deserialize to
     * @param base64String the serialized object
     * @param <T> the array's type
     * @return the deserialized array, or an empty array if the specified string is null or empty
     */
    public static <T> T[] arrayFromBase64(TypeToken<T> type, String base64String) throws IOException, ClassNotFoundException {
        if (base64String == null || base64String.isEmpty()) {
            return ArrayUtil.createArray(type, 0);
        }

        return BukkitSerializer.tryInputStream(base64String, (input) -> {
            final int length = input.readInt();
            final T[] array = ArrayUtil.createArray(type, length);
            for (int i = 0; i < length; i++) {
                final T object = (T) input.readObject();
                array[i] = object;
            }
            return array;
        });
    }

    /**
     * Deserializes the specified base64 {@link String} to a {@link List} of the provided type.
     *
     * @param type the {@link TypeToken} of the List's type to deserialize to
     * @param base64String the serialized object
     * @param <T> the List's type
     * @return the deserialized List, or an empty list if the specified string is null or empty
     */
    public static <T> List<T> listFromBase64(TypeToken<T> type, String base64String) throws IOException, ClassNotFoundException {
        if (base64String == null || base64String.isEmpty()) {
            return Lists.newArrayList();
        }
        final T[] array = BukkitSerializer.arrayFromBase64(type, base64String);
        return Arrays.asList(array);
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
        final ItemStack[] compressed = BukkitSerializer.compressItemStackArray(itemStackArray);
        return Arrays.asList(compressed);
    }

    private static String tryOutputStream(CheckedConsumer<BukkitObjectOutputStream, IOException> consumer)
        throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(outputStream)
        ) {
            consumer.accept(bukkitOutput);
            final byte[] outputBytes = outputStream.toByteArray();
            return Base64Coder.encodeLines(outputBytes);
        }
    }

    private static <T> T tryInputStream(String base64String, CheckedFunction<BukkitObjectInputStream, T, IOException> function)
        throws IOException, ClassNotFoundException {
        final byte[] data = Base64Coder.decodeLines(base64String);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream)
        ) {
            return function.apply(bukkitInputStream);
        }
    }

}