package io.prodity.commons.spigot.legacy.utils;

import io.prodity.commons.spigot.legacy.tryto.CheckedConsumer;
import java.util.function.Consumer;

public enum Empty {

    ;

    public static <T> Consumer<T> consumer() {
        return (obj) -> {
        };
    }

    public static <T> CheckedConsumer<T> checkedConsumer() {
        return (obj) -> {
        };
    }

}
