package io.prodity.commons.spigot.legacy.tryto;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Throwable;

}
