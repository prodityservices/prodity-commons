package io.prodity.commons.spigot.v1_12_R1.gui;

import com.google.common.base.Preconditions;
import io.prodity.commons.inject.Export;
import io.prodity.commons.spigot.gui.GuiProvider;
import io.prodity.commons.spigot.inject.McVersion;
import io.prodity.commons.spigot.inject.ProdityVersions;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jvnet.hk2.annotations.Service;

@Export
@McVersion(ProdityVersions.V1_12)
@Service
@SuppressWarnings("Duplicates")
public class NmsGuiProvider implements GuiProvider {

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


    private static String getProtocolType(Inventory inventory) {
        switch (inventory.getType()) {
            case ANVIL:
                return "minecraft:anvil";
            case BEACON:
                return "minecraft:beacon";
            case BREWING:
                return "minecraft:brewing_stand";
            case CHEST:
                return "minecraft:chest";
            case DISPENSER:
                return "minecraft:dispenser";
            case DROPPER:
                return "minecraft:dropper";
            case ENCHANTING:
                return "minecraft:enchanting_table";
            case FURNACE:
                return "minecraft:furnace";
            case HOPPER:
                return "minecraft:hopper";
            case MERCHANT:
                return "minecraft:villager";
            case WORKBENCH:
                return "minecraft:crafting_table";
            default:
                return "minecraft:container";
        }
    }
}
