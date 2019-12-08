package io.prodity.commons.spigot.serialize;

import com.google.common.base.Preconditions;
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

/**
 * Utilities for serializing & deserializing Bukkit objects.
 */
public enum BukkitSerializer {

    ;

    /**
     * Serializes the specified {@link Object} to a byte array using a {@link BukkitObjectOutputStream}.
     *
     * @param object the object
     * @return the serialized object
     */
    public static <T> byte[] objectToBytes(T object) throws IOException {
        Preconditions.checkNotNull(object, "object");
        return BukkitSerializer.tryOutputStream((bukkitOutput) -> bukkitOutput.writeObject(object));
    }

    /**
     * Serializes the specified {@link Object} to base64 using a {@link BukkitObjectOutputStream}.
     *
     * @param object the object
     * @return the serialized object in base64
     */
    public static <T> String objectToBase64(T object) throws IOException {
        Preconditions.checkNotNull(object, "object");
        final byte[] bytes = BukkitSerializer.objectToBytes(object);
        return Base64Coder.encodeLines(bytes);
    }

    /**
     * Serializes the specified array to a byte array using a {@link BukkitObjectOutputStream}.
     *
     * @param array the array
     * @return the serialized object, or an empty array if the specified object array is empty
     */
    public static <T> byte[] arrayToBytes(T[] array) throws IOException {
        Preconditions.checkNotNull(array, "array");

        if (array.length == 0) {
            return new byte[]{};
        }

        return BukkitSerializer.tryOutputStream((bukkitOutput) -> {
            bukkitOutput.writeInt(array.length);
            for (T object : array) {
                bukkitOutput.writeObject(object);
            }
        });
    }

    /**
     * Serializes the specified array to base64 using a {@link BukkitObjectOutputStream}.
     *
     * @param array the array
     * @return the serialized array in base64, or an empty string if the specified array is empty
     */
    public static <T> String arrayToBase64(T[] array) throws IOException {
        Preconditions.checkNotNull(array, "array");
        final byte[] bytes = BukkitSerializer.arrayToBytes(array);
        return bytes.length == 0 ? "" : Base64Coder.encodeLines(bytes);
    }

    /**
     * Serializes the specified {@link Collection} to a byte array using a {@link BukkitObjectOutputStream}.
     *
     * @param collection the collection
     * @return the serialized collection, or an empty byte array if the specified collection is empty
     */
    public static <T> byte[] collectionToBytes(Collection<? extends T> collection) throws IOException {
        Preconditions.checkNotNull(collection, "collection");
        if (collection.isEmpty()) {
            return new byte[]{};
        }
        return BukkitSerializer.arrayToBytes(collection.toArray());
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
     * Deserializes the specified byte array to the provided type.
     *
     * @param type the {@link TypeToken} of the type to deserialize to
     * @param bytes the serialized object
     * @param <T> the type to deserialize to
     * @return the deserialized object, or null if the specified byte array is null or empty
     */
    @Nullable
    public static <T> T objectFromBytes(TypeToken<T> type, @Nullable byte[] bytes) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return BukkitSerializer.tryInputStream(bytes, (input) -> {
            return (T) input.readObject();
        });
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
    public static <T> T objectFromBase64(TypeToken<T> type, @Nullable String base64String) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }

        final byte[] bytes = Base64Coder.decodeLines(base64String);
        return BukkitSerializer.objectFromBytes(type, bytes);
    }

    /**
     * Deserializes the specified byte array to an array of the provided type.
     *
     * @param type the {@link TypeToken} of the array's type to deserialize to
     * @param bytes the serialized object
     * @param <T> the array's type
     * @return the deserialized array, or an empty array if the specified byte array is null or empty
     */
    public static <T> T[] arrayFromBytes(TypeToken<T> type, @Nullable byte[] bytes) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        if (bytes == null || bytes.length == 0) {
            return ArrayUtil.createArray(type, 0);
        }

        return BukkitSerializer.tryInputStream(bytes, (input) -> {
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
     * Deserializes the specified base64 {@link String} to an array of the provided type.
     *
     * @param type the {@link TypeToken} of the array's type to deserialize to
     * @param base64String the serialized object
     * @param <T> the array's type
     * @return the deserialized array, or an empty array if the specified string is null or empty
     */
    public static <T> T[] arrayFromBase64(TypeToken<T> type, @Nullable String base64String) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        if (base64String == null || base64String.isEmpty()) {
            return ArrayUtil.createArray(type, 0);
        }

        final byte[] bytes = Base64Coder.decodeLines(base64String);
        return BukkitSerializer.arrayFromBytes(type, bytes);
    }

    /**
     * Deserializes the specified byte array to a {@link List} of the provided type.
     *
     * @param type the {@link TypeToken} of the List's type to deserialize to
     * @param bytes the serialized object
     * @param <T> the List's type
     * @return the deserialized List, or an empty list if the specified byte array is null or empty
     */
    public static <T> List<T> listFromBytes(TypeToken<T> type, byte[] bytes) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(type, "type");
        final T[] array = BukkitSerializer.arrayFromBytes(type, bytes);
        return Arrays.asList(array);
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
        Preconditions.checkNotNull(type, "type");
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

    private static byte[] tryOutputStream(CheckedConsumer<BukkitObjectOutputStream, IOException> consumer)
        throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(outputStream)
        ) {
            consumer.accept(bukkitOutput);
            final byte[] outputBytes = outputStream.toByteArray();
            return outputBytes;
        }
    }

    private static <T> T tryInputStream(byte[] bytes, CheckedFunction<BukkitObjectInputStream, T, IOException> function)
        throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(bytes, "bytes");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream)
        ) {
            return function.apply(bukkitInputStream);
        }
    }

}