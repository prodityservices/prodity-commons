package io.prodity.commons.spigot.legacy.tryto;

@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Throwable;

}
