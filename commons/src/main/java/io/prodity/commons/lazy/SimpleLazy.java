package io.prodity.commons.lazy;

import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.annotation.Nullable;

/**
 * A thread safe implementation of {@link Lazy}.
 *
 * @param <V> the type of value
 */
public class SimpleLazy<V> implements Lazy<V> {

    private final Supplier<V> supplier;
    private final AtomicReference<V> valueReference;
    private final AtomicBoolean valid;

    public SimpleLazy(Supplier<V> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        this.supplier = supplier;
        this.valid = new AtomicBoolean(false);
        this.valueReference = new AtomicReference<>();
    }

    @Override
    @Nullable
    public V get() {
        if (!this.valid.get()) {
            final V newValue = this.supplier.get();
            this.valid.set(true);
        }
        return this.valueReference.get();
    }

    @Override
    public void invalidate() {
        this.valid.set(false);
    }

}