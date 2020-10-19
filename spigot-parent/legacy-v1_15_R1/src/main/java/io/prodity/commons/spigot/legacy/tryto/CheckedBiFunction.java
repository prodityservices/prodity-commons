package io.prodity.commons.spigot.legacy.tryto;

@FunctionalInterface
public interface CheckedBiFunction<T1, T2, R> {

    R apply(T1 t1, T2 t2) throws Throwable;

}
