package io.prodity.commons.spigot.legacy.reference;

public interface MutableReference<T> extends Reference<T> {

    void set(T referent);

}
