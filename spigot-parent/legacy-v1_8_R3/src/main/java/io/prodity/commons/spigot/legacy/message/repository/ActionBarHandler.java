package io.prodity.commons.spigot.legacy.message.repository;

import io.prodity.commons.spigot.legacy.message.replace.PlayerReplacer;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.message.send.ActionBarSender;
import java.util.function.Function;
import org.bukkit.entity.Player;

public class ActionBarHandler {

    private final MessageRepository messages;

    public ActionBarHandler(MessageRepository messages) {
        this.messages = messages;
    }

    protected void sendMessage(Function<Player, String> messageFunction, Player... players) {
        for (Player player : players) {
            this.sendMessage(messageFunction, player);
        }
    }

    protected void sendMessage(Function<Player, String> messageFunction, Iterable<? extends Player> players) {
        for (Player player : players) {
            this.sendMessage(messageFunction, player);
        }
    }

    protected void sendMessage(Function<Player, String> messageFunction, Player player) {
        final String components = messageFunction.apply(player);
        ActionBarSender.send(player, components);
    }

    public void send(String key, Player... players) {
        final String message = this.messages.get(key);
        this.sendMessage((player) -> message, players);
    }

    public void send(String key, Replacer replacer, Player... players) {
        final String message = this.messages.get(key);
        final Function<Player, String> function = this.createFunction(replacer, message);
        this.sendMessage(function, players);
    }

    public void send(String key, Iterable<? extends Player> players) {
        final String message = this.messages.get(key);
        this.sendMessage((player) -> message, players);
    }

    public void send(String key, Replacer replacer, Iterable<? extends Player> players) {
        final String message = this.messages.get(key);
        final Function<Player, String> function = this.createFunction(replacer, message);
        this.sendMessage(function, players);
    }

    public void papiSend(String key, Player... players) {
        this.send(key, PlayerReplacer.createWithPapi(), players);
    }

    public void papiSend(String key, Replacer replacer, Player... players) {
        this.send(key, replacer.clone().papi(true), players);
    }

    public void papiSend(String key, Iterable<? extends Player> players) {
        this.send(key, PlayerReplacer.createWithPapi(), players);
    }

    public void papiSend(String key, Replacer replacer, Iterable<? extends Player> players) {
        this.send(key, replacer.clone().papi(true), players);
    }

    private Function<Player, String> createFunction(Replacer replacer, String message) {
        return (player) -> {
            final String messageToSend = replacer.replace(player, message);
            return messageToSend;
        };
    }

}