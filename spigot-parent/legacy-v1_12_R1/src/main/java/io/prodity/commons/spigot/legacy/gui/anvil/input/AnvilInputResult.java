package io.prodity.commons.spigot.legacy.gui.anvil.input;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class AnvilInputResult<T> {

    public static <T> AnvilInputResult<T> valid(T value) {
        Preconditions.checkNotNull(value, "value");
        return new AnvilInputResult<>(value, null);
    }

    public static <T> AnvilInputResult<T> invalid(String errorKey) {
        Preconditions.checkNotNull(errorKey, "errorKey");
        return new AnvilInputResult<>(null, errorKey);
    }

    private final T value;
    private final String errorKey;

    private AnvilInputResult(@Nullable T value, @Nullable String errorKey) {
        this.value = value;
        this.errorKey = errorKey;
    }

    @Nullable
    public T getValue() {
        return this.value;
    }

    @Nullable
    public String getErrorKey() {
        return this.errorKey;
    }

    public boolean isValid() {
        return this.value != null;
    }
    
}