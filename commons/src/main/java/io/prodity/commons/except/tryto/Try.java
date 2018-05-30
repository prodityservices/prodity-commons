package io.prodity.commons.except.tryto;

import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public enum Try {

    ;

    /**
     * Wraps the specified {@link CheckedRunnable} in a new {@link Runnable} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param runnable the {@link CheckedRunnable}
     * @return the created {@link Runnable}
     */
    public static Runnable to(CheckedRunnable.GenericCheckedRunnable runnable) {
        return Try.to(runnable, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedRunnable} in a new {@link Runnable} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param runnable the {@link CheckedRunnable}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link Runnable}
     */
    public static Runnable to(CheckedRunnable.GenericCheckedRunnable runnable, @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(runnable, "runnable");

        return () -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
        };
    }

    /**
     * Wraps the specified {@link CheckedRunnable} in a new {@link CheckedRunnable} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param runnable the {@link CheckedRunnable}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedRunnable}
     */
    public static <E extends Throwable> CheckedRunnable<E> mapExceptionTo(CheckedRunnable.GenericCheckedRunnable runnable,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedSupplier} in a new {@link Supplier} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param supplier the {@link CheckedSupplier}
     * @return the created {@link Supplier}
     */
    public static <T> Supplier<T> to(CheckedSupplier.GenericCheckedSupplier<T> supplier) {
        Preconditions.checkNotNull(supplier, "supplier");
        return Try.to(supplier, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedSupplier} in a new {@link Supplier} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param supplier the {@link CheckedSupplier}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link Supplier}
     */
    public static <T> Supplier<T> to(CheckedSupplier.GenericCheckedSupplier<T> supplier, @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(supplier, "supplier");

        return () -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
            return null;
        };
    }

    /**
     * Wraps the specified {@link CheckedSupplier} in a new {@link CheckedSupplier} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param supplier the {@link CheckedSupplier}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedSupplier}
     */
    public static <T, E extends Throwable> CheckedSupplier<T, E> mapExceptionTo(CheckedSupplier.GenericCheckedSupplier<T> supplier,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedFunction} in a new {@link Function} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param function the {@link CheckedFunction}
     * @return the created {@link Function}
     */
    public static <T, R> Function<T, R> to(CheckedFunction.GenericCheckedFunction<T, R> function) {
        Preconditions.checkNotNull(function, "function");
        return Try.to(function, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedFunction} in a new {@link Function} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param function the {@link CheckedFunction}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link Function}
     */
    public static <T, R> Function<T, R> to(CheckedFunction.GenericCheckedFunction<T, R> function,
        @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(function, "function");

        return (value) -> {
            try {
                return function.apply(value);
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
            return null;
        };
    }

    /**
     * Wraps the specified {@link CheckedFunction} in a new {@link CheckedFunction} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param function the {@link CheckedFunction}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedFunction}
     */
    public static <T, R, E extends Throwable> CheckedFunction<T, R, E> mapExceptionTo(CheckedFunction.GenericCheckedFunction<T, R> function,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedBiFunction} in a new {@link BiFunction} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param biFunction the {@link CheckedBiFunction}
     * @return the created {@link BiFunction}
     */
    public static <T1, T2, R> BiFunction<T1, T2, R> to(CheckedBiFunction.GenericCheckedBiFunction<T1, T2, R> biFunction) {
        Preconditions.checkNotNull(biFunction, "biFunction");
        return Try.to(biFunction, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedBiFunction} in a new {@link BiFunction} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param biFunction the {@link CheckedBiFunction}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link BiFunction}
     */
    public static <T1, T2, R> BiFunction<T1, T2, R> to(CheckedBiFunction.GenericCheckedBiFunction<T1, T2, R> biFunction,
        @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(biFunction, "biFunction");

        return (value1, value2) -> {
            try {
                return biFunction.apply(value1, value2);
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
            return null;
        };
    }

    /**
     * Wraps the specified {@link CheckedBiFunction} in a new {@link CheckedBiFunction} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param biFunction the {@link CheckedBiFunction}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedBiFunction}
     */
    public static <T1, T2, R, E extends Throwable> CheckedBiFunction<T1, T2, R, E> mapExceptionTo(
        CheckedBiFunction.GenericCheckedBiFunction<T1, T2, R> biFunction,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedConsumer} in a new {@link Consumer} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param consumer the {@link CheckedConsumer}
     * @return the created {@link Consumer}
     */
    public static <T> Consumer<T> to(CheckedConsumer.GenericCheckedConsumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        return Try.to(consumer, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedConsumer} in a new {@link Consumer} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param consumer the {@link CheckedConsumer}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link Consumer}
     */
    public static <T> Consumer<T> to(CheckedConsumer.GenericCheckedConsumer<T> consumer, @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(consumer, "consumer");

        return (value) -> {
            try {
                consumer.accept(value);
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
        };
    }

    /**
     * Wraps the specified {@link CheckedConsumer} in a new {@link CheckedConsumer} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param consumer the {@link CheckedConsumer}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedConsumer}
     */
    public static <T, E extends Throwable> CheckedConsumer<T, E> mapExceptionTo(CheckedConsumer.GenericCheckedConsumer<T> consumer,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedBiConsumer} in a new {@link BiConsumer} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param biConsumer the {@link CheckedBiConsumer}
     * @return the created {@link BiConsumer}
     */
    public static <T1, T2> BiConsumer<T1, T2> to(CheckedBiConsumer.GenericCheckedBiConsumer<T1, T2> biConsumer) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        return Try.to(biConsumer, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedBiConsumer} in a new {@link BiConsumer} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param biConsumer the {@link CheckedBiConsumer}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link BiConsumer}
     */
    public static <T1, T2> BiConsumer<T1, T2> to(CheckedBiConsumer.GenericCheckedBiConsumer<T1, T2> biConsumer,
        @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");

        return (value1, value2) -> {
            try {
                biConsumer.accept(value1, value2);
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
        };
    }

    /**
     * Wraps the specified {@link CheckedBiConsumer} in a new {@link CheckedBiConsumer} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param biConsumer the {@link CheckedBiConsumer}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedBiConsumer}
     */
    public static <T1, T2, E extends Throwable> CheckedBiConsumer<T1, T2, E> mapExceptionTo(
        CheckedBiConsumer.GenericCheckedBiConsumer<T1, T2> biConsumer,
        Function<Throwable, E> exceptionMapper) {
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

    /**
     * Wraps the specified {@link CheckedPredicate} in a new {@link Predicate} that propagates any checked {@link Exception}s to a {@link RuntimeException}.
     *
     * @param predicate the {@link CheckedPredicate}
     * @return the created {@link Predicate}
     */
    public static <T> Predicate<T> to(CheckedPredicate.GenericCheckedPredicate<T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        return Try.to(predicate, Try::propagateThrow);
    }

    /**
     * Wraps the specified {@link CheckedPredicate} in a new {@link Predicate} that handles the any {@link Throwable} the
     * specified runnable throws using the specified {@link Consumer}.
     *
     * @param predicate the {@link CheckedPredicate}
     * @param throwableHandler the {@link Consumer} to accept any throwable thrown by the specified runnable
     * @return the created {@link Predicate}
     */
    public static <T> Predicate<T> to(CheckedPredicate.GenericCheckedPredicate<T> predicate,
        @Nullable Consumer<Throwable> throwableHandler) {
        Preconditions.checkNotNull(predicate, "predicate");

        return (value) -> {
            try {
                return predicate.test(value);
            } catch (Throwable throwable) {
                if (throwableHandler != null) {
                    throwableHandler.accept(throwable);
                }
            }
            return false;
        };
    }

    /**
     * Wraps the specified {@link CheckedPredicate} in a new {@link CheckedPredicate} that maps any {@link Throwable} thrown to
     * another exception type using the specified {@link Function}.
     *
     * @param predicate the {@link CheckedPredicate}
     * @param exceptionMapper the {@link Function} used to map any thrown {@link Throwable}
     * @return the newly created {@link CheckedPredicate}
     */
    public static <T, E extends Throwable> CheckedPredicate<T, E> mapExceptionTo(CheckedPredicate.GenericCheckedPredicate<T> predicate,
        Function<Throwable, E> exceptionMapper) {
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

    public static void propagateThrow(Throwable throwable) {
        throw Try.propagate(throwable);
    }

    public static RuntimeException propagate(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

    public static <T extends Throwable> T propagateTo(Throwable throwable, Function<Throwable, T> function) {
        final T typedThrowable = function.apply(throwable);
        return typedThrowable.getClass().isInstance(throwable) ? (T) throwable : typedThrowable;
    }

}