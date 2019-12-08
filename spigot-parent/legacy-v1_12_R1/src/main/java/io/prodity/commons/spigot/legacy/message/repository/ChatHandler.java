package io.prodity.commons.spigot.legacy.message.repository;

import io.prodity.commons.spigot.legacy.message.replace.PlayerReplacer;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.message.send.ChatSender;
import java.util.function.Function;
import org.bukkit.command.CommandSender;

public class ChatHandler {

    private final MessageRepository messages;

    public ChatHandler(MessageRepository messages) {
        this.messages = messages;
    }

    protected void sendMessage(Function<CommandSender, String> messageFunction, CommandSender... commandSenders) {
        for (CommandSender commandSender : commandSenders) {
            this.sendMessage(messageFunction, commandSender);
        }
    }

    protected void sendMessage(Function<CommandSender, String> messageFunction, Iterable<? extends CommandSender> commandSenders) {
        for (CommandSender commandSender : commandSenders) {
            this.sendMessage(messageFunction, commandSender);
        }
    }

    protected void sendMessage(Function<CommandSender, String> messageFunction, CommandSender commandSender) {
        final String message = messageFunction.apply(commandSender);
        ChatSender.send(commandSender, message);
    }

    public void send(String key, CommandSender... commandSenders) {
        final String message = this.messages.get(key);
        this.sendMessage((sender) -> message, commandSenders);
    }

    public void send(String key, Replacer replacer, CommandSender... commandSenders) {
        final String message = this.messages.get(key);
        this.sendMessage((sender) -> replacer.replace(sender, message), commandSenders);
    }

    public void send(String key, Iterable<? extends CommandSender> commandSenders) {
        final String message = this.messages.get(key);
        this.sendMessage((sender) -> message, commandSenders);
    }

    public void send(String key, Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        final String message = this.messages.get(key);
        this.sendMessage((sender) -> replacer.replace(sender, message), commandSenders);
    }

    public void papiSend(String key, CommandSender... commandSenders) {
        this.send(key, PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSend(String key, Replacer replacer, CommandSender... commandSenders) {
        this.send(key, replacer.clone().papi(true), commandSenders);
    }

    public void papiSend(String key, Iterable<? extends CommandSender> commandSenders) {
        this.send(key, PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSend(String key, Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        this.send(key, replacer.clone().papi(true), commandSenders);
    }

}