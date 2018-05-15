package io.prodity.commons.lazy;

import com.google.common.base.Preconditions;
import io.prodity.commons.time.TimeUtil;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.annotation.Nullable;

/**
 * A {@link Lazy} implementation in which the cached value times out after the specified amount of time. Is also thread safe.
 */
public class TimedLazy<V> implements Lazy<V> {

    private final Supplier<V> valueSupplier;

    private final TimeUnit timeoutUnit;
    private final long timeoutDuration;

    private final AtomicReference<Instant> validUntil;
    private final AtomicReference<V> value;

    public TimedLazy(Supplier<V> valueSupplier, TimeUnit timeoutUnit, long timeoutDuration) {
        Preconditions.checkNotNull(valueSupplier, "valueSupplier");
        Preconditions.checkNotNull(timeoutUnit, "timeoutUnit");

        this.valueSupplier = valueSupplier;
        this.timeoutUnit = timeoutUnit;
        this.timeoutDuration = timeoutDuration;

        this.validUntil = new AtomicReference<>();
        this.value = new AtomicReference<>();
    }

    public long getTimeout(TimeUnit unit) {
        Preconditions.checkNotNull(unit, "unit");
        return unit.convert(this.timeoutDuration, this.timeoutUnit);
    }

    @Override
    @Nullable
    public V get() {
        final Instant validUntilInstant = this.validUntil.get();
        if (validUntilInstant == null || !this.isValid(validUntilInstant)) {
            final V value = this.valueSupplier.get();
            this.value.set(value);
            this.validUntil.set(this.getNextExpireInstant());
            return value;
        }

        return this.value.get();
    }

    private boolean isValid(Instant validUntilInstant) {
        return Instant.now().isAfter(validUntilInstant);
    }

    private Instant getNextExpireInstant() {
        return Instant.now().plus(this.timeoutDuration, TimeUtil.toChronoUnit(this.timeoutUnit));
    }

    /**
     * Times the value out.
     */
    @Override
    public void invalidate() {
        this.validUntil.set(Instant.now().minusMillis(1L));
    }

}