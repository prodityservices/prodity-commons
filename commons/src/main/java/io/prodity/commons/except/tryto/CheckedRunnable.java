package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedRunnable<E extends Throwable> {

    void run() throws E;

    @FunctionalInterface
    interface GenericCheckedRunnable extends CheckedRunnable<Throwable> {

    }

}