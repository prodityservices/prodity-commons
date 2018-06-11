package io.prodity.commons.spigot.legacy.tryto;

@FunctionalInterface
public interface CheckedPredicate<T> {

    boolean test(T t);

    default CheckedPredicate<T> negate() {
        return (value) -> !this.test(value);
    }

}
