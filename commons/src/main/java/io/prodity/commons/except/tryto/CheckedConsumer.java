package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedConsumer<T, E extends Throwable> {

    void accept(T t) throws E;

    @FunctionalInterface
    interface GenericCheckedConsumer<T> extends CheckedConsumer<T, Throwable> {

    }

}