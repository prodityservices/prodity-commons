package io.prodity.commons.future;

import javax.annotation.Nullable;

public enum Futures {

    ;

    @Nullable
    public static <T> T printError(@Nullable T value, @Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
        return value;
    }
    
}