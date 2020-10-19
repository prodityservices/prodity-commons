package io.prodity.commons.spigot.legacy.plugin;

import io.prodity.commons.spigot.legacy.tryto.CheckedRunnable;
import io.prodity.commons.spigot.legacy.tryto.Try;

import java.util.function.Supplier;

public interface Loadable extends CheckedRunnable {

    static <T extends Loadable> T createAndLoad(Supplier<T> instanceSupplier) throws Throwable {
        final T loadable = instanceSupplier.get();
        loadable.load();
        return loadable;
    }

    static <T extends Loadable> T createAndPropogateLoad(Supplier<T> instanceSupplier) {
        return Try.get(() -> Loadable.createAndLoad(instanceSupplier));
    }

    static <T extends Loadable> void propogateLoad(T loadable) {
        Try.run(loadable::load);
    }

    void load() throws Throwable;

    @Override
    default void run() throws Throwable {
        this.load();
    }

}
