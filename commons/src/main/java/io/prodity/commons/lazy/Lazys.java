package io.prodity.commons.lazy;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public enum Lazys {

    ;

    /**
     * Creates a new {@link Lazy} with the specified {@link Supplier}.
     *
     * @param supplier the supplier
     * @return the created lazy
     */
    public static <V> Lazy<V> of(Supplier<V> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        return new SimpleLazy<>(supplier);
    }

    /**
     * Creates a constant {@link Lazy} that always caches the same value.
     *
     * @param value the
     * @param <V> the type of value
     * @return the created lazy
     */
    public static <V> Lazy<V> constant(@Nullable V value) {
        return Lazys.of(() -> value);
    }

    /**
     * Creates a new {@link TimedLazy} with the specified values.
     *
     * @param supplier the value {@link Supplier}
     * @param milliseconds the amount of {@link TimeUnit#MILLISECONDS} the value times out after
     * @param <V> the value type
     * @return the created lazy
     */
    public static <V> TimedLazy<V> timed(Supplier<V> supplier, long milliseconds) {
        return new TimedLazy<>(supplier, TimeUnit.MILLISECONDS, milliseconds);
    }

    /**
     * Creates a new {@link TimedLazy} with the specified values.
     *
     * @param supplier the value {@link Supplier}
     * @param timeoutUnit the {@link TimeUnit} of the specified duration
     * @param timeoutDuration the duration of which cached values timeout after
     * @param <V> the value type
     * @return the created lazy
     */
    public static <V> TimedLazy<V> timed(Supplier<V> supplier, TimeUnit timeoutUnit, long timeoutDuration) {
        return new TimedLazy<>(supplier, timeoutUnit, timeoutDuration);
    }

}