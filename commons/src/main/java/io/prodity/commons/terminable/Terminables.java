package io.prodity.commons.terminable;

import io.prodity.commons.except.tryto.Try;
import java.util.function.Consumer;

public enum Terminables {

    ;

    public static Terminable wrap(AutoCloseable autoCloseable) {
        if (autoCloseable instanceof Terminable) {
            return (Terminable) autoCloseable;
        }
        return Terminables.wrap(autoCloseable, Try.to(AutoCloseable::close));
    }

    public static <T> Terminable wrap(T object, Consumer<T> terminator) {
        return new TerminableWrapper(object, terminator);
    }

    public static void closeSilently(Terminable... terminables) {
        for (Terminable terminable : terminables) {
            terminable.closeSilently();
        }
    }

    public static void closeSilently(Iterable<Terminable> terminables) {
        for (Terminable terminable : terminables) {
            terminable.closeSilently();
        }
    }

    public static void closeAndPrintException(Terminable... terminables) {
        for (Terminable terminable : terminables) {
            terminable.closeAndPrintException();
        }
    }

    public static void closeAndPrintException(Iterable<Terminable> terminables) {
        for (Terminable terminable : terminables) {
            terminable.closeAndPrintException();
        }
    }

}