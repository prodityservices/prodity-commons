package io.prodity.commons.tryto;

@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Throwable;

}
