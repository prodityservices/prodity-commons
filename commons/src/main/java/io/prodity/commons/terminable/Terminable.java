package io.prodity.commons.terminable;

import java.util.Optional;

@FunctionalInterface
public interface Terminable extends AutoCloseable {

    default boolean isClosed() {
        return false;
    }

    default Optional<Throwable> closeSilently() {
        try {
            this.close();
            return Optional.empty();
        } catch (Throwable throwable) {
            return Optional.of(throwable);
        }
    }

    default void closeAndPrintException() {
        this.closeSilently().ifPresent(Throwable::printStackTrace);
    }


}