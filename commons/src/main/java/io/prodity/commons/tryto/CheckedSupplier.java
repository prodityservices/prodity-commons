package io.prodity.commons.tryto;

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;

}