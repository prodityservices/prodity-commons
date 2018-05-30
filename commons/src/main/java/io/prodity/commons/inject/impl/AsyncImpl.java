package io.prodity.commons.inject.impl;

import io.prodity.commons.except.tryto.CheckedBiConsumer;
import io.prodity.commons.except.tryto.CheckedConsumer;
import io.prodity.commons.except.tryto.CheckedFunction;
import io.prodity.commons.inject.Async;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncImpl<T> implements Async<T> {

    private final T instance;
    private final Executor executor;

    public AsyncImpl(T instance, Executor executor) {
        this.instance = instance;
        this.executor = executor;
    }

    @Override
    public <IN> CompletableFuture<Void> forEach(Collection<IN> collection, CheckedBiConsumer<T, IN, Throwable> consumer) {
        return CompletableFuture.allOf(collection.stream()
            .map(element -> this.run(inst -> consumer.accept(inst, element)))
            .toArray(CompletableFuture[]::new));
    }

    @Override
    public CompletableFuture<Void> run(CheckedConsumer<T, Throwable> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.executor.execute(() -> {
            try {
                consumer.accept(this.instance);
                future.complete(null);
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public <V> CompletableFuture<V> get(CheckedFunction<T, V, Throwable> function) {
        CompletableFuture<V> future = new CompletableFuture<>();
        this.executor.execute(() -> {
            try {
                future.complete(function.apply(this.instance));
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
