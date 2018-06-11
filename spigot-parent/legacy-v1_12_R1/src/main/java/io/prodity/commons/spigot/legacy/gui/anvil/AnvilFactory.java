package io.prodity.commons.spigot.legacy.gui.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AnvilFactory {

    AnvilGuiInventory createInventory(Player player);

    void fakeSetItem(Player player, int slot, ItemStack itemStack);

    void registerGui(AnvilGUI gui);

    void unregisterGui(AnvilGUI gui);

}