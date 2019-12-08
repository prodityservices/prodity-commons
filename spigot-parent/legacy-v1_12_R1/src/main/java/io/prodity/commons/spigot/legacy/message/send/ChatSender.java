package io.prodity.commons.spigot.legacy.message.send;

import org.bukkit.command.CommandSender;

public enum ChatSender {

    ;

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

}