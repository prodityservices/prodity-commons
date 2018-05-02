package io.prodity.commons.tryto;

@FunctionalInterface
public interface CheckedBiConsumer<T1, T2> {

    void accept(T1 t1, T2 t2) throws Throwable;

}