package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedBiFunction<T1, T2, R, E extends Throwable> {

    R apply(T1 t1, T2 t2) throws E;

    @FunctionalInterface
    interface GenericCheckedBiFunction<T1, T2, R> extends CheckedBiFunction<T1, T2, R, Throwable> {

    }

}