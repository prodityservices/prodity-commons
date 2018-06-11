package io.prodity.commons.spigot.legacy.reference;

import io.prodity.commons.spigot.legacy.reference.impl.SimpleImmutableReference;
import io.prodity.commons.spigot.legacy.reference.impl.SimpleMutableReference;

public interface Reference<T> {

    static <T1> MutableReference<T1> mutable() {
        return new SimpleMutableReference<>(null);
    }

    static <T1> MutableReference<T1> mutable(T1 value) {
        return new SimpleMutableReference<>(value);
    }

    static <T1> ImmutableReference<T1> immutable() {
        return new SimpleImmutableReference<>(null);
    }

    static <T1> ImmutableReference<T1> immutable(T1 value) {
        return new SimpleImmutableReference<>(value);
    }

    T get();

    MutableReference<T> toMutable();

    ImmutableReference<T> toImmutable();

}
