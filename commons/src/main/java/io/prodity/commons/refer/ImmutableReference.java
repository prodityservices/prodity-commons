package io.prodity.commons.refer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * A thread-safe immutable implementation of {@link Reference}.
 */
public class ImmutableReference<T> implements Reference<T> {

    private final T referent;

    public ImmutableReference() {
        this(null);
    }

    public ImmutableReference(@Nullable T referent) {
        this.referent = referent;
    }

    @Override
    @Nullable
    public T get() {
        return this.referent;
    }

    @Override
    public boolean isPresent() {
        return this.referent != null;
    }

    @Override
    public void apply(Consumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        consumer.accept(this.referent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.referent);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImmutableReference)) {
            return false;
        }
        final ImmutableReference<?> that = (ImmutableReference<?>) object;
        return Objects.equals(this.referent, that.referent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("referent", this.referent)
            .toString();
    }

}