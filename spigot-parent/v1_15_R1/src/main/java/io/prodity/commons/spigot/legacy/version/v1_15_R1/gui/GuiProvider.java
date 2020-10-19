package io.prodity.commons.spigot.legacy.version.v1_15_R1.gui;

import com.google.common.base.Preconditions;
import io.prodity.commons.spigot.v1_15_R1.gui.NmsGuiProvider;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
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

        final MinecraftKey mcKey = IRegistry.MENU.keySet().stream()
          .filter(key -> key.getKey().contains(protocolType.replace("minecraft:", "")))
          .findFirst()
          .orElse(null);

        if (mcKey == null) {
            return;
        }

        final PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, IRegistry.MENU.get(mcKey), titleMessage);

        entityPlayer.playerConnection.sendPacket(packet);
        entityPlayer.updateInventory(entityPlayer.activeContainer);

    }

}