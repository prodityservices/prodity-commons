package io.prodity.commons.lazy;

import javax.annotation.Nullable;

/**
 * Caches a value, but does not do so until the value is explicitly called for.
 *
 * @param <V> the type of value
 */
public interface Lazy<V> {

    /**
     * Retreives this value,
     */
    @Nullable
    V get();

    /**
     * Invalidates the cached value.
     */
    void invalidate();

}