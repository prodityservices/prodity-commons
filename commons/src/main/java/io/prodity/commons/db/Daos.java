package io.prodity.commons.db;

import javax.annotation.Nullable;

public enum Daos {

    ;

    @Nullable
    public static <T> T printError(@Nullable T value, @Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
        return value;
    }

}