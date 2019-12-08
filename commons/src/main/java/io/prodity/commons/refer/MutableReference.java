package io.prodity.commons.refer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * A thread-safe mutable implementation of {@link Reference}.
 */
public class MutableReference<T> implements Reference<T> {

    private final AtomicReference<T> referent;

    public MutableReference() {
        this(null);
    }

    public MutableReference(@Nullable T referent) {
        this.referent = new AtomicReference<>(referent);
    }

    @Override
    @Nullable
    public T get() {
        return this.referent.get();
    }

    @Override
    public boolean isPresent() {
        return this.referent.get() != null;
    }

    public void set(@Nullable T referent) {
        this.referent.set(referent);
    }

    @Override
    public void apply(Consumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        consumer.accept(this.referent.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.referent);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableReference)) {
            return false;
        }
        final MutableReference<?> that = (MutableReference<?>) object;
        return Objects.equals(this.referent, that.referent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("referent", this.referent)
            .toString();
    }

}