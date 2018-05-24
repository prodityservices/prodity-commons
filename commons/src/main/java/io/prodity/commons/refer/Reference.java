package io.prodity.commons.refer;

import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * Represents a reference to an object.
 *
 * @param <T> the type of object referred to
 */
public interface Reference<T> {

    @Nullable
    T get();

    /**
     * Gets whether or not the referent object is present (nonnull).
     *
     * @return true if the referent object is not null
     */
    boolean isPresent();
    
    void apply(Consumer<T> consumer);

}