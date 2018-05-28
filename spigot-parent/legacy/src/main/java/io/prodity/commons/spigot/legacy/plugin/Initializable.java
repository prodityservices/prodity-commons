package io.prodity.commons.spigot.legacy.plugin;

import io.prodity.commons.spigot.legacy.tryto.Try;
import java.util.function.Supplier;

public interface Initializable {

    static <T extends Initializable> T createAndInit(Supplier<T> supplier) {
        final T object = supplier.get();
        object.initialize();
        return object;
    }

    static <T extends Initializable> T createAndInitLoad(Supplier<T> supplier) {
        final T object = supplier.get();
        if (object instanceof Loadable) {
            Try.to(((Loadable) object)::load).run();
        }
        object.initialize();
        return object;
    }

    void initialize();

}
