package io.prodity.commons.except.tryto;

import com.google.common.base.Preconditions;
import io.prodity.commons.except.tryto.CheckedBiConsumer.GenericCheckedBiConsumer;
import io.prodity.commons.except.tryto.CheckedBiFunction.GenericCheckedBiFunction;
import io.prodity.commons.except.tryto.CheckedConsumer.GenericCheckedConsumer;
import io.prodity.commons.except.tryto.CheckedFunction.GenericCheckedFunction;
import io.prodity.commons.except.tryto.CheckedPredicate.GenericCheckedPredicate;
import io.prodity.commons.except.tryto.CheckedRunnable.GenericCheckedRunnable;
import io.prodity.commons.except.tryto.CheckedSupplier.GenericCheckedSupplier;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public enum Try {

    ;

    public static Runnable to(@Nonnull GenericCheckedRunnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");

        return () -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <E extends Throwable> CheckedRunnable<E> mapExceptionTo(@Nonnull GenericCheckedRunnable runnable,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(runnable, "runnable");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");

        return () -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T> Supplier<T> to(@Nonnull GenericCheckedSupplier<T> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, E extends Throwable> CheckedSupplier<T, E> mapExceptionTo(@Nonnull GenericCheckedSupplier<T> supplier,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(supplier, "supplier");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T, R> Function<T, R> to(@Nonnull GenericCheckedFunction<T, R> function) {
        Preconditions.checkNotNull(function, "function");
        return (value) -> {
            try {
                return function.apply(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, R, E extends Throwable> CheckedFunction<T, R, E> mapExceptionTo(@Nonnull GenericCheckedFunction<T, R> function,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(function, "function");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return (object) -> {
            try {
                return function.apply(object);
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> to(@Nonnull GenericCheckedBiFunction<T1, T2, R> biFunction) {
        Preconditions.checkNotNull(biFunction, "biFunction");
        return (value1, value2) -> {
            try {
                return biFunction.apply(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2, R, E extends Throwable> CheckedBiFunction<T1, T2, R, E> mapExceptionTo(
        @Nonnull GenericCheckedBiFunction<T1, T2, R> biFunction,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(biFunction, "biFunction");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return (object1, object2) -> {
            try {
                return biFunction.apply(object1, object2);
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T> Consumer<T> to(@Nonnull GenericCheckedConsumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        return (value) -> {
            try {
                consumer.accept(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, E extends Throwable> CheckedConsumer<T, E> mapExceptionTo(@Nonnull GenericCheckedConsumer<T> consumer,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(consumer, "consumer");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return (object) -> {
            try {
                consumer.accept(object);
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T1, T2> BiConsumer<T1, T2> to(@Nonnull GenericCheckedBiConsumer<T1, T2> biConsumer) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        return (value1, value2) -> {
            try {
                biConsumer.accept(value1, value2);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T1, T2, E extends Throwable> CheckedBiConsumer<T1, T2, E> mapExceptionTo(
        @Nonnull GenericCheckedBiConsumer<T1, T2> biConsumer,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return (object1, object2) -> {
            try {
                biConsumer.accept(object1, object2);
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    public static <T> Predicate<T> to(@Nonnull GenericCheckedPredicate<T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        return (value) -> {
            try {
                return predicate.test(value);
            } catch (Throwable throwable) {
                throw Try.propagate(throwable);
            }
        };
    }

    public static <T, E extends Throwable> CheckedPredicate<T, E> mapExceptionTo(@Nonnull GenericCheckedPredicate<T> predicate,
        @Nonnull Function<Throwable, E> exceptionMapper) {
        Preconditions.checkNotNull(predicate, "predicate");
        Preconditions.checkNotNull(exceptionMapper, "exceptionMapper");
        return (object) -> {
            try {
                return predicate.test(object);
            } catch (Throwable throwable) {
                throw Try.propagateTo(throwable, exceptionMapper);
            }
        };
    }

    private static RuntimeException propagate(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

    private static <T extends Throwable> T propagateTo(Throwable throwable, Function<Throwable, T> function) {
        final T typedThrowable = function.apply(throwable);
        return typedThrowable.getClass().isInstance(throwable) ? (T) throwable : typedThrowable;
    }

}