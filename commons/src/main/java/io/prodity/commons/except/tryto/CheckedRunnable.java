package io.prodity.commons.except.tryto;

@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Throwable;

}
