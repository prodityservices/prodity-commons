package io.prodity.commons.spigot.legacy.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntUtils {

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static int getIntForInventory(int size) {
        while (size % 9 != 0) {
            size++;
        }

        return size;
    }

}
