package io.prodity.commons.spigot.legacy.gui.anvil;

import org.bukkit.inventory.AnvilInventory;

public interface AnvilGuiInventory extends AnvilInventory {

    void addListener(AnvilInputListener listener);

    void removeListener(AnvilInputListener listener);

    String getCurrentText();

    void setCurrentText(String text);

    void open();

}