package io.prodity.commons.except.tryto;

import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public enum Try {

    ;

    public static Runnable to(@Nonnull CheckedRunnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        return () -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static Runnable runnable(@Nonnull CheckedRunnable runnable) {
        return Try.to(runnable);
    }

    public static void run(@Nonnull CheckedRunnable runnable) {
        Try.to(runnable).run();
    }

    public static <T> Supplier<T> to(@Nonnull CheckedSupplier<T> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Supplier<T> supplier(@Nonnull CheckedSupplier<T> supplier) {
        return Try.to(supplier);
    }

    public static <T> T get(@Nonnull CheckedSupplier<T> supplier) {
        return Try.to(supplier).get();
    }

    public static <T, R> Function<T, R> to(@Nonnull CheckedFunction<T, R> function) {
        Preconditions.checkNotNull(function, "function");
        return (value) -> {
            try {
                return function.apply(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, R> Function<T, R> function(@Nonnull CheckedFunction<T, R> function) {
        return Try.to(function);
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> to(@Nonnull CheckedBiFunction<T1, T2, R> biFunction) {
        Preconditions.checkNotNull(biFunction, "biFunction");
        return (value1, value2) -> {
            try {
                return biFunction.apply(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> biFunction(@Nonnull CheckedBiFunction<T1, T2, R> biFunction) {
        return Try.to(biFunction);
    }

    public static <T> Consumer<T> to(@Nonnull CheckedConsumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        return (value) -> {
            try {
                consumer.accept(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Consumer<T> consumer(@Nonnull CheckedConsumer<T> consumer) {
        return Try.to(consumer);
    }

    public static <T1, T2> BiConsumer<T1, T2> to(@Nonnull CheckedBiConsumer<T1, T2> biConsumer) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        return (value1, value2) -> {
            try {
                biConsumer.accept(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2> BiConsumer<T1, T2> biConsumer(@Nonnull CheckedBiConsumer<T1, T2> biConsumer) {
        return Try.to(biConsumer);
    }

    public static <T> Predicate<T> to(@Nonnull CheckedPredicate<T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        return (value) -> {
            try {
                return predicate.test(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T> Predicate<T> predicate(@Nonnull CheckedPredicate<T> predicate) {
        return Try.to(predicate);
    }

    public static <T> Predicate<T> toAndNegate(@Nonnull CheckedPredicate<T> predicate) {
        return Try.to(predicate.negate());
    }

    private static RuntimeException propagate(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

}