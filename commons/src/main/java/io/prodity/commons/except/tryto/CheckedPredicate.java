package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedPredicate<T, E extends Throwable> {

    boolean test(T t) throws E;

    default CheckedPredicate<T, E> negate() {
        return (value) -> !this.test(value);
    }

    @FunctionalInterface
    interface GenericCheckedPredicate<T> extends CheckedPredicate<T, Throwable> {

        @Override
        default GenericCheckedPredicate<T> negate() {
            return (value) -> !this.test(value);
        }

    }

}