package io.prodity.commons.spigot.legacy.tryto;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum Try {

    ;

    public static Runnable to(CheckedRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static Runnable runnable(CheckedRunnable runnable) {
        return Try.to(runnable);
    }

    public static void run(CheckedRunnable runnable) {
        Try.to(runnable).run();
    }

    public static <T> T get(CheckedSupplier<T> supplier) {
        return Try.to(supplier).get();
    }

    public static <T> Supplier<T> to(CheckedSupplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return Try.to(supplier);
    }

    public static <T, R> Function<T, R> to(CheckedFunction<T, R> function) {
        return (value) -> {
            try {
                return function.apply(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return Try.to(function);
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> to(CheckedBiFunction<T1, T2, R> biFunction) {
        return (value1, value2) -> {
            try {
                return biFunction.apply(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> biFunction(CheckedBiFunction<T1, T2, R> biFunction) {
        return Try.to(biFunction);
    }


    public static <T> Consumer<T> to(CheckedConsumer<T> consumer) {
        return (value) -> {
            try {
                consumer.accept(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return Try.to(consumer);
    }

    public static <T1, T2> BiConsumer<T1, T2> to(CheckedBiConsumer<T1, T2> biConsumer) {
        return (value1, value2) -> {
            try {
                biConsumer.accept(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2> BiConsumer<T1, T2> biConsumer(CheckedBiConsumer<T1, T2> biConsumer) {
        return Try.to(biConsumer);
    }

    public static <T> Predicate<T> to(CheckedPredicate<T> predicate) {
        return (value) -> {
            try {
                return predicate.test(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return Try.to(predicate);
    }

    public static <T> Predicate<T> toAndNegate(CheckedPredicate<T> predicate) {
        return Try.to(predicate.negate());
    }

    public static <T> Predicate<T> negate(Predicate<T> predicate) {
        return predicate.negate();
    }

    private static RuntimeException propagate(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

}