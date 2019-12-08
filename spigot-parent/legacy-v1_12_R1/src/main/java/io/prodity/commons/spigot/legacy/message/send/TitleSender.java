package io.prodity.commons.spigot.legacy.message.send;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TitleSender {

    ;

    public static void send(CommandSender commandSender, TitleTimes times, String title, String subTitle) {
        if (commandSender instanceof Player) {
            TitleSender.send((Player) commandSender, times, title, subTitle);
        }
    }

    public static void send(Player player, TitleTimes times, String title, String subTitle) {
        player.sendTitle(TitleSender.getMessage(title), TitleSender.getMessage(subTitle), times.show(), times.stay(), times.fade());
    }

    private static String getMessage(String message) {
        return message == null ? "" : message;
    }

}