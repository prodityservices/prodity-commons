package io.prodity.commons.spigot.legacy.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.function.Supplier;

@UtilityClass
public class StringUtils {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String repeat(String message, int amount) {
        return org.apache.commons.lang.StringUtils.repeat(message, amount);
    }

    public static String capitalize(String message) {
        return org.apache.commons.lang.StringUtils.capitalize(message);
    }

    public static String fastReplace(String string, String target, String replacement) {
        return StringUtils.fastReplace(string, target, () -> replacement);
    }

    public static String fastReplace(String string, String target, Supplier<String> replacementSupplier) {
        final int targetLength = target.length();

        if (targetLength == 0) {
            return string;
        }

        int indexOne = string.indexOf(target);
        if (indexOne < 0) {
            return string;
        }

        final StringBuilder builder = new StringBuilder();
        int currentIndex = 0;

        final String replacement = replacementSupplier.get();

        do {
            builder.append(string, currentIndex, indexOne);
            builder.append(replacement);
            currentIndex = indexOne + targetLength;
            indexOne = string.indexOf(target, currentIndex);
        } while (indexOne > 0);

        builder.append(string, currentIndex, string.length());
        return builder.toString();
    }

}
