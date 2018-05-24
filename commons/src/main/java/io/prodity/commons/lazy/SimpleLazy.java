package io.prodity.commons.lazy;

import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A thread safe implementation of {@link Lazy} implemented using double-checked
 * locking. Does not permit null values.
 *
 * @param <V> the type of value
 */
public class SimpleLazy<V> implements Lazy<V> {
    private final Supplier<V> supplier;
    private volatile V value = null;

    public SimpleLazy(Supplier<V> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        this.supplier = supplier;
    }

    @Override
    @Nonnull
    public V get() {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    this.value = this.supplier.get();
                    Preconditions.checkNotNull(this.value, "Supplier returned null!");
                }
            }
        }
        return this.value;
    }

    @Override
    public void invalidate() {
        this.value = null;
    }

}