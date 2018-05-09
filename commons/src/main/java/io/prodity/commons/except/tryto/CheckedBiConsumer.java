package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedBiConsumer<T1, T2, E extends Throwable> {

    void accept(T1 t1, T2 t2) throws E;

    @FunctionalInterface
    interface GenericCheckedBiConsumer<T1, T2> extends CheckedBiConsumer<T1, T2, Throwable> {

    }

}