package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedFunction<T, R, E extends Throwable> {

    R apply(T t) throws E, ClassNotFoundException;

    @FunctionalInterface
    interface GenericCheckedFunction<T, R> extends CheckedFunction<T, R, Throwable> {

    }

}