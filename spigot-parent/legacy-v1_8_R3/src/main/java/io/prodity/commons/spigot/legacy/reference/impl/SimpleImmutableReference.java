package io.prodity.commons.spigot.legacy.reference.impl;

import io.prodity.commons.spigot.legacy.reference.ImmutableReference;
import io.prodity.commons.spigot.legacy.reference.MutableReference;

public class SimpleImmutableReference<T> implements ImmutableReference<T> {

    private final T referent;

    public SimpleImmutableReference(T referent) {
        this.referent = referent;
    }

    @Override
    public T get() {
        return this.referent;
    }

    @Override
    public MutableReference<T> toMutable() {
        return new SimpleMutableReference(this.referent);
    }

    @Override
    public ImmutableReference<T> toImmutable() {
        return this;
    }

}