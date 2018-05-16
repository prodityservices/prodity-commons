package io.prodity.commons.terminable;

import java.util.function.Consumer;

public class TerminableWrapper<T> implements Terminable {

    private final T object;
    private final Consumer<T> terminator;

    protected TerminableWrapper(T object, Consumer<T> terminator) {
        this.object = object;
        this.terminator = terminator;
    }

    public T getObject() {
        return this.object;
    }

    public Consumer<T> getTerminator() {
        return this.terminator;
    }

    @Override
    public void close() {
        this.terminator.accept(this.object);
    }

}