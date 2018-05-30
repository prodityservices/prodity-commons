package io.prodity.commons.inject;

import io.prodity.commons.except.tryto.CheckedBiConsumer;
import io.prodity.commons.except.tryto.CheckedConsumer;
import io.prodity.commons.except.tryto.CheckedFunction;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Wrapper for performing asynchronous operations on an injected service.
 *
 * @param <T> the wrapped type
 */
public interface Async<T> {

    /**
     * Concurrently applies the provided consumer to each element in the collection.  There is no guarantee
     * about the ordering of the application or what thread the consumer will be applied on.
     *
     * @param collection collection
     * @param consumer consumer to apply
     * @param <IN> the type of elements in the collection
     * @return a CompletableFuture that completes after all elements in the collection have been processed
     */
    <IN> CompletableFuture<Void> forEach(Collection<IN> collection, CheckedBiConsumer<T, IN, Throwable> consumer);

    /**
     * Applies the provided consumer to the backing value asynchronously.
     *
     * @param consumer consumer to apply
     * @return CompletableFuture that completes after the consumer has been applied.
     */
    CompletableFuture<Void> run(CheckedConsumer<T, Throwable> consumer);

    /**
     * Applies the provided function to the backing value asynchronously.
     *
     * @param function function to apply
     * @param <V> the return type of the function
     * @return CompletableFuture that completes with the return value of the function
     */
    <V> CompletableFuture<V> get(CheckedFunction<T, V, Throwable> function);
}
