package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Throwable;

}