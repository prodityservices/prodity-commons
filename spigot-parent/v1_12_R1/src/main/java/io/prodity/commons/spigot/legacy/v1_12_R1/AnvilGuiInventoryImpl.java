package io.prodity.commons.spigot.legacy.v1_12_R1;

import io.prodity.commons.spigot.legacy.gui.anvil.AnvilGuiInventory;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilInputListener;
import javax.annotation.Nullable;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;
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
    public void setCurrentText(@Nullable String text) {
        this.anvil.currentName = text;
        final ItemStack item = this.getItem(0);
        if (item == null) {
            return;
        }
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName((text == null) ? "" : text);
        item.setItemMeta(itemMeta);
        this.setItem(0, item);
    }

    @Override
    public void open() {
        final EntityPlayer player = this.anvil.player;
        final int count = player.nextContainerCounter();

        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(count, "minecraft:anvil", new ChatMessage("Anvil")));
        player.activeContainer = this.anvil;
        player.activeContainer.windowId = count;
        player.activeContainer.addSlotListener(player);
    }

}
