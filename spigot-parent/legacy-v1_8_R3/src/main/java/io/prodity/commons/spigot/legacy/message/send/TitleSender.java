package io.prodity.commons.spigot.legacy.message.send;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public enum TitleSender {

    ;

    public static void send(CommandSender commandSender, TitleTimes times, String title, String subTitle) {
        if (commandSender instanceof Player) {
            TitleSender.send((Player) commandSender, times, title, subTitle);
        }
    }

    public static void send(Player player, TitleTimes times, String title, String subTitle) {
        TitleSender.sendTitleTimes(times.show(), times.stay(), times.fade(), player);
        TitleSender.sendSubTitle(TitleSender.getMessage(subTitle), player);
        TitleSender.sendTitle(TitleSender.getMessage(title), player);
    }

    private static void sendTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks, Player player) {
        final PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(fadeInTicks, stayTicks,
            fadeOutTicks);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(timesPacket);
    }

    private static void sendTitle(String title, Player player) {
        final PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, TitleSender.createRawComponent(title));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }

    private static void sendSubTitle(String title, Player player) {
        final PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, TitleSender.createRawComponent(title));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
    }

    private static IChatBaseComponent createRawComponent(final String rawText) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", rawText);
        return ChatSerializer.a(jsonObject.toString());
    }

    private static String getMessage(String message) {
        return message == null ? "" : message;
    }

}