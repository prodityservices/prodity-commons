package io.prodity.commons.spigot.gui;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface GuiProvider  {

    void updateInventoryTitle(Player player, Inventory inventory, String newTitle);
}