package io.prodity.commons.spigot.legacy.lazy;

public interface LazyValue<T> {

    void setAndInit(T value);

    T get();

    void update();

    T updateAndGet();

    boolean isInitialized();

}