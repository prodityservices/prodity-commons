package io.prodity.commons.spigot.legacy.gui.anvil;

public interface AnvilInputListener {

    default void onNameChange(String oldName, String name) {
    }

    default void onInvalidName(String name) {
    }

}