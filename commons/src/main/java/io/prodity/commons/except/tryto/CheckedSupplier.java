package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;

}