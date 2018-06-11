package io.prodity.commons.spigot.legacy.lazy;

import com.google.common.base.Objects;
import java.util.function.Supplier;
import lombok.Getter;

public class SimpleLazyValue<T> implements LazyValue<T> {

    private final Supplier<T> valueSupplier;

    @Getter
    private boolean initialized = false;

    private T value;

    public SimpleLazyValue(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Override
    public void setAndInit(T value) {
        this.value = value;
        this.initialized = true;
    }

    @Override
    public T get() {
        if (!this.initialized) {
            this.value = this.valueSupplier.get();
            this.initialized = true;
        }
        return this.value;
    }

    @Override
    public void update() {
        this.value = this.valueSupplier.get();
        if (!this.initialized) {
            this.initialized = true;
        }
    }

    @Override
    public T updateAndGet() {
        this.update();
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.valueSupplier, this.initialized, this.value);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof SimpleLazyValue)) {
            return false;
        }

        final SimpleLazyValue that = (SimpleLazyValue) object;

        return Objects.equal(this.valueSupplier, that.valueSupplier) &&
            Objects.equal(this.initialized, that.initialized) &&
            Objects.equal(this.value, that.value);
    }

}