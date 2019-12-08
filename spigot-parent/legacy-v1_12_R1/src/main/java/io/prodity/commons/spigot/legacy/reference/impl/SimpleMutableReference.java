package io.prodity.commons.spigot.legacy.reference.impl;

import io.prodity.commons.spigot.legacy.reference.ImmutableReference;
import io.prodity.commons.spigot.legacy.reference.MutableReference;

public class SimpleMutableReference<T> implements MutableReference<T> {

    private T referent;

    public SimpleMutableReference(T referent) {
        this.referent = referent;
    }

    @Override
    public void set(T referent) {
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
        return new SimpleImmutableReference<>(this.referent);
    }

}