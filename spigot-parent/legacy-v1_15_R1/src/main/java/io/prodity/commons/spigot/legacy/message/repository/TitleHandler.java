package io.prodity.commons.spigot.legacy.message.repository;

import io.prodity.commons.spigot.legacy.message.replace.PlayerReplacer;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.message.send.TitleSender;
import io.prodity.commons.spigot.legacy.message.send.TitleTimes;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class TitleHandler {

    private final MessageRepository messages;

    public TitleHandler(MessageRepository messages) {
        this.messages = messages;
    }

    protected void sendMessage(Function<Player, String> titleFunction, Function<Player, String> subTitleFunction, TitleTimes times,
        Player... players) {
        for (Player player : players) {
            this.sendMessage(titleFunction, subTitleFunction, times, player);
        }
    }

    protected void sendMessage(Function<Player, String> titleFunction, Function<Player, String> subTitleFunction, TitleTimes times,
        Iterable<? extends Player> players) {
        for (Player player : players) {
            this.sendMessage(titleFunction, subTitleFunction, times, player);
        }
    }

    protected void sendMessage(Function<Player, String> titleFunction, Function<Player, String> subTitleFunction, TitleTimes times,
        Player player) {
        final String title = titleFunction.apply(player);
        final String subTitle = subTitleFunction.apply(player);
        TitleSender.send(player, times, title, subTitle);
    }

    private String getString(String key) {
        return key == null ? null : this.messages.get(key);
    }

    private Function<Player, String> createFunction(Replacer replacer, String message) {
        return (player) -> message == null ? null : replacer.replace(player, message);
    }

    public void send(String titleKey, String subTitleKey, TitleTimes times, Player... players) {
        final String title = this.getString(titleKey);
        final String subTitle = this.getString(subTitleKey);
        this.sendMessage((player) -> title, (player) -> subTitle, times, players);
    }

    public void send(String titleKey, String subTitleKey, TitleTimes times, Replacer replacer, Player... players) {
        final String title = this.getString(titleKey);
        final String subTitle = this.getString(subTitleKey);
        this.sendMessage(this.createFunction(replacer, title), this.createFunction(replacer, subTitle), times, players);
    }

    public void send(String titleKey, String subTitleKey, TitleTimes times, Iterable<? extends Player> players) {
        final String title = this.getString(titleKey);
        final String subTitle = this.getString(subTitleKey);
        this.sendMessage((player) -> title, (player) -> subTitle, times, players);
    }

    public void send(String titleKey, String subTitleKey, TitleTimes times, Replacer replacer, Iterable<? extends Player> players) {
        final String title = this.getString(titleKey);
        final String subTitle = this.getString(subTitleKey);
        this.sendMessage(this.createFunction(replacer, title), this.createFunction(replacer, subTitle), times, players);
    }

    public void papiSend(String titleKey, String subTitleKey, TitleTimes times, Player... players) {
        this.send(titleKey, subTitleKey, times, PlayerReplacer.createWithPapi(), players);
    }

    public void papiSend(String titleKey, String subTitleKey, TitleTimes times, Replacer replacer, Player... players) {
        this.send(titleKey, subTitleKey, times, replacer.clone().papi(true), players);
    }

    public void papiSend(String titleKey, String subTitleKey, TitleTimes times, Iterable<? extends Player> players) {
        this.send(titleKey, subTitleKey, times, PlayerReplacer.createWithPapi(), players);
    }

    public void papiSend(String titleKey, String subTitleKey, TitleTimes times, Replacer replacer, Iterable<? extends Player> players) {
        this.send(titleKey, subTitleKey, times, replacer.clone().papi(true), players);
    }

}
