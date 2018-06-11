package io.prodity.commons.spigot.legacy.message.send;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum ActionBarSender {

    ;

    public static void send(CommandSender sender, BaseComponent[] components) {
        if (sender instanceof Player) {
            ActionBarSender.send((Player) sender, components);
        }
    }

    public static void send(Player player, BaseComponent[] components) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
    }

    public static void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            ActionBarSender.send((Player) sender, message);
        }
    }

    public static void send(Player player, String message) {
        final BaseComponent[] components = TextComponent.fromLegacyText(message);
        ActionBarSender.send(player, components);
    }

}