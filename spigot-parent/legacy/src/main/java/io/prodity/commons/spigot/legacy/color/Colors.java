package io.prodity.commons.spigot.legacy.color;

import io.prodity.commons.spigot.legacy.color.impl.SimpleImmutableColor;
import io.prodity.commons.spigot.legacy.color.impl.SimpleMutableColor;
import io.prodity.commons.spigot.legacy.random.RandomUtil;
import org.apache.commons.lang3.tuple.Triple;

public enum Colors {

    ;

    public static void verifyComponent(int component, String componentName) throws IllegalArgumentException {
        if (component < 0) {
            throw new IllegalArgumentException(
                "the Color component '" + componentName + "' is '" + component + "' (<0) but must be between 0-255 (inclusive)");
        }
        if (component > 255) {
            throw new IllegalArgumentException(
                "the Color component '" + componentName + "' is '" + component + "' (>255) but must be between 0-255 (inclusive)");
        }
    }

    public static ImmutableColor immutableFromRgb(int red, int green, int blue) {
        return new SimpleImmutableColor(red, green, blue);
    }

    private static Triple<Integer, Integer, Integer> fromHex(String hex) throws IllegalArgumentException {
        if (hex.length() != 6 && hex.length() != 7) {
            throw new IllegalArgumentException("hex string '" + hex + "' isn't in proper hex format (000000, #000000, etc.)");
        }
        if (hex.startsWith("#")) {
            hex = hex.substring(1, hex.length());
        }
        final int red = Integer.valueOf(hex.substring(0, 2), 16);
        final int green = Integer.valueOf(hex.substring(2, 4), 16);
        final int blue = Integer.valueOf(hex.substring(4, 6), 16);
        return Triple.of(red, green, blue);
    }

    public static int getRandomComponentValue() {
        return RandomUtil.getRandomIntInRange(0, 256);
    }

    public static ImmutableColor immutableRandom() {
        return Colors
            .immutableFromRgb(Colors.getRandomComponentValue(), Colors.getRandomComponentValue(), Colors.getRandomComponentValue());
    }

    public static ImmutableColor immutableFromHex(String hex) throws IllegalArgumentException {
        final Triple<Integer, Integer, Integer> triple = Colors.fromHex(hex);
        return new SimpleImmutableColor(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    public static ImmutableColor immutableFromBukkit(org.bukkit.Color color) {
        return new SimpleImmutableColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static ImmutableColor immutableFrom(Color color) {
        return new SimpleImmutableColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static MutableColor mutableRandom() {
        return Colors
            .mutableFromRgb(Colors.getRandomComponentValue(), Colors.getRandomComponentValue(), Colors.getRandomComponentValue());
    }

    public static MutableColor mutableFromRgb(int red, int green, int blue) {
        return new SimpleMutableColor(red, green, blue);
    }

    public static MutableColor mutableFromHex(String hex) {
        final Triple<Integer, Integer, Integer> triple = Colors.fromHex(hex);
        return new SimpleMutableColor(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    public static MutableColor mutableFromBukkit(org.bukkit.Color color) {
        return new SimpleMutableColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static MutableColor mutableFrom(Color color) {
        return new SimpleMutableColor(color.getRed(), color.getGreen(), color.getBlue());
    }

}