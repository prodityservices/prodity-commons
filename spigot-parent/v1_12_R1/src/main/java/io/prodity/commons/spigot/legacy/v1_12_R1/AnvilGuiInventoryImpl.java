package io.prodity.commons.spigot.legacy.v1_12_R1;

import io.prodity.commons.spigot.legacy.gui.anvil.AnvilGuiInventory;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilInputListener;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryAnvil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilGuiInventoryImpl extends CraftInventoryAnvil implements AnvilGuiInventory {

    private final ContainerAnvilImpl anvil;

    public AnvilGuiInventoryImpl(ContainerAnvilImpl anvil) {
        super(null, anvil.craftInventory, anvil.resultInventory, anvil);
        this.anvil = anvil;
    }

    @Override
    public void addListener(AnvilInputListener listener) {
        this.anvil.listeners.add(listener);
    }

    @Override
    public void removeListener(AnvilInputListener listener) {
        this.anvil.listeners.remove(listener);
    }

    @Override
    public String getCurrentText() {
        return this.anvil.currentName;
    }

    @Override
    public void setCurrentText(String text) {
        if (text != null) {
            if (text.startsWith(ChatColor.RESET.toString())) {
                text = text.substring(2);
            } else if (text.trim().equals(ChatColor.COLOR_CHAR)) {
                text = "";
            }
        }
        this.anvil.currentName = text;
        ItemStack item = this.getItem(0);
        if (item == null) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(((text == null) || text.isEmpty()) ? ChatColor.RESET.toString() : text);
        item.setItemMeta(itemMeta);
        this.setItem(0, item);
    }

    @Override
    public void open() {
        final EntityPlayer player = this.anvil.player;
        final int count = player.nextContainerCounter();

        //If anvil titles ever work be sure to include that.
        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(count, "minecraft:anvil", new ChatMessage("Anvil")));
        player.activeContainer = this.anvil;
        player.activeContainer.windowId = count;
        player.activeContainer.addSlotListener(player);
    }

}
