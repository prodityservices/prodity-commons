package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {

    T get() throws E;

    @FunctionalInterface
    interface GenericCheckedSupplier<T> extends CheckedSupplier<T, Throwable> {

    }

}