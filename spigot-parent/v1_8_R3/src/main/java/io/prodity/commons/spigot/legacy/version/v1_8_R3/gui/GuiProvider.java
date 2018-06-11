package io.prodity.commons.spigot.legacy.version.v1_8_R3.gui;

import com.google.common.base.Preconditions;
import io.prodity.commons.spigot.v1_8_R3.gui.NmsGuiProvider;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiProvider extends io.prodity.commons.spigot.legacy.gui.GuiProvider {

    @SuppressWarnings("Duplicates")
    @Override
    public void updateInventoryTitle(Player player, Inventory inventory, String newTitle) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(inventory, "inventory");
        Preconditions.checkNotNull(newTitle, "newTitle");

        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        final int windowId = entityPlayer.activeContainer.windowId;
        final String protocolType = NmsGuiProvider.getProtocolType(inventory);
        final ChatMessage titleMessage = new ChatMessage(newTitle);
        final int size = inventory.getSize();

        final PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, protocolType, titleMessage, size);

        entityPlayer.playerConnection.sendPacket(packet);
        entityPlayer.updateInventory(entityPlayer.activeContainer);
    }

}