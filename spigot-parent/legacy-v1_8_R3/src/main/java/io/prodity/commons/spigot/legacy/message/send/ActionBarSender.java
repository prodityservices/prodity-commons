package io.prodity.commons.spigot.legacy.message.send;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public enum ActionBarSender {

    ;

    public static void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            ActionBarSender.sendActionBarMsg((Player) sender, message);
        }
    }

    public static void send(Player player, String message) {
        ActionBarSender.sendActionBarMsg(player, message);
    }

    private static void sendActionBarMsg(Player player, String message) {
        final PacketPlayOutChat barPacket = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(barPacket);
    }

}