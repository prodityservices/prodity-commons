package io.prodity.commons.spigot.legacy.tryto;

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;

}
