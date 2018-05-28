package io.prodity.commons.spigot.legacy.plugin;

public interface Terminable {

    static <T extends Terminable> void terminateSafely(T... terminables) {
        for (Terminable terminable : terminables) {
            if (terminable != null) {
                terminable.terminate();
            }
        }
    }

    static void terminateSafely(Iterable<? extends Terminable> terminables) {
        for (Terminable terminable : terminables) {
            if (terminable != null) {
                terminable.terminate();
            }
        }
    }

    void terminate();

}