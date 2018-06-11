package io.prodity.commons.spigot.legacy.message.composite;

import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.message.replace.PlayerReplacer;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.message.send.ActionBarSender;
import io.prodity.commons.spigot.legacy.message.send.ChatSender;
import io.prodity.commons.spigot.legacy.message.send.TitleSender;
import io.prodity.commons.spigot.legacy.message.send.TitleTimes;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public class MessageComposite {

    public static final class Builder {

        private String chatMessage;
        private String titleMessage;
        private String subTitleMessage;
        private String actionBarMessage;
        private TitleTimes titleTimes;

        private Builder() {
        }

        public Builder chatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
            return this;
        }

        public Builder titleMessage(String titleMessage) {
            this.titleMessage = titleMessage;
            return this;
        }

        public Builder subTitleMessage(String subTitleMessage) {
            this.subTitleMessage = subTitleMessage;
            return this;
        }

        public Builder actionBarMessage(String actionBarMessage) {
            this.actionBarMessage = actionBarMessage;
            return this;
        }

        public Builder titleTimes(TitleTimes titleTimes) {
            this.titleTimes = titleTimes;
            return this;
        }

        public MessageComposite build() {
            return new MessageComposite(this.chatMessage, this.actionBarMessage, this.titleMessage, this.subTitleMessage,
                this.titleTimes);
        }

    }

    public static Builder builder() {
        return new MessageComposite.Builder();
    }

    private final Optional<String> chatMessage;
    private final Optional<String> actionBarMessage;
    private final Optional<String> titleMessage;
    private final Optional<String> subTitleMessage;
    private final TitleTimes titleTimes;

    private MessageComposite(String chatMessage, String actionBarMessage, String titleMessage, String subTitleMessage,
        TitleTimes titleTimes) {
        this.chatMessage = Optional.ofNullable(chatMessage);
        this.actionBarMessage = Optional.ofNullable(actionBarMessage);
        this.titleMessage = Optional.ofNullable(titleMessage);
        this.subTitleMessage = Optional.ofNullable(subTitleMessage);
        this.titleTimes = Optional.ofNullable(titleTimes).orElseGet(() -> TitleTimes.of(0, 100, 20));
    }

    public Optional<String> getChatMessage() {
        return this.chatMessage;
    }

    public Optional<String> getActionBarMessage() {
        return this.actionBarMessage;
    }

    public Optional<String> getTitleMessage() {
        return this.titleMessage;
    }

    public Optional<String> getSubTitleMessage() {
        return this.subTitleMessage;
    }

    public void send(CommandSender... commandSenders) {
        this.send(null, commandSenders);
    }

    public void send(Replacer replacer, CommandSender... commandSenders) {
        final Iterable<? extends CommandSender> iterable = Lists.newArrayList(commandSenders);
        this.send(replacer, iterable);
    }

    public void send(Iterable<? extends CommandSender> commandSenders) {
        this.send(null, commandSenders);
    }

    public void send(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        for (CommandSender commandSender : commandSenders) {
            this.internalSendChat(commandSender, replacer);
            this.internalSendActionBar(commandSender, replacer);
            this.internalSendTitle(commandSender, replacer);
        }
    }

    public void papiSend(CommandSender... commandSenders) {
        this.send(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSend(Replacer replacer, CommandSender... commandSenders) {
        this.send(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void papiSend(Iterable<? extends CommandSender> commandSenders) {
        this.send(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSend(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        this.send(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void sendChat(CommandSender... commandSenders) {
        this.sendChat(null, commandSenders);
    }

    public void sendChat(Replacer replacer, CommandSender... commandSenders) {
        final Iterable<? extends CommandSender> iterable = Lists.newArrayList(commandSenders);
        this.sendChat(replacer, iterable);
    }

    public void sendChat(Iterable<? extends CommandSender> commandSenders) {
        this.sendChat(null, commandSenders);
    }

    public void sendChat(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        commandSenders.forEach((commandSender) -> this.internalSendChat(commandSender, replacer));
    }

    public void papiSendChat(CommandSender... commandSenders) {
        this.sendChat(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendChat(Replacer replacer, CommandSender... commandSenders) {
        this.sendChat(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void papiSendChat(Iterable<? extends CommandSender> commandSenders) {
        this.sendChat(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendChat(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        this.sendChat(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void sendTitle(CommandSender... commandSenders) {
        this.sendTitle(null, commandSenders);
    }

    public void sendTitle(Replacer replacer, CommandSender... commandSenders) {
        final Iterable<? extends CommandSender> iterable = Lists.newArrayList(commandSenders);
        this.sendTitle(replacer, iterable);
    }

    public void sendTitle(Iterable<? extends CommandSender> commandSenders) {
        this.sendTitle(null, commandSenders);
    }

    public void sendTitle(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        commandSenders.forEach((commandSender) -> this.internalSendTitle(commandSender, replacer));
    }

    public void papiSendTitle(CommandSender... commandSenders) {
        this.sendTitle(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendTitle(Replacer replacer, CommandSender... commandSenders) {
        this.sendTitle(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void papiSendTitle(Iterable<? extends CommandSender> commandSenders) {
        this.sendTitle(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendTitle(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        this.sendTitle(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void sendActionBar(CommandSender... commandSenders) {
        this.sendActionBar(null, commandSenders);
    }

    public void sendActionBar(Replacer replacer, CommandSender... commandSenders) {
        final Iterable<? extends CommandSender> iterable = Lists.newArrayList(commandSenders);
        this.sendActionBar(replacer, iterable);
    }

    public void sendActionBar(Iterable<? extends CommandSender> commandSenders) {
        this.sendActionBar(null, commandSenders);
    }

    public void sendActionBar(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        commandSenders.forEach((commandSender) -> this.internalSendActionBar(commandSender, replacer));
    }

    public void papiSendActionBar(CommandSender... commandSenders) {
        this.sendActionBar(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendActionBar(Replacer replacer, CommandSender... commandSenders) {
        this.sendActionBar(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    public void papiSendActionBar(Iterable<? extends CommandSender> commandSenders) {
        this.sendActionBar(PlayerReplacer.createWithPapi(), commandSenders);
    }

    public void papiSendActionBar(Replacer replacer, Iterable<? extends CommandSender> commandSenders) {
        this.sendActionBar(this.resolveNullablePapiReplacer(replacer), commandSenders);
    }

    private void internalSendChat(CommandSender commandSender, Replacer replacer) {
        this.chatMessage
            .map((message) -> replacer == null ? message : replacer.replace(commandSender, message))
            .ifPresent((message) -> ChatSender.send(commandSender, message));
    }

    private void internalSendTitle(CommandSender commandSender, Replacer replacer) {
        if (this.titleMessage.isPresent() || this.subTitleMessage.isPresent()) {
            final String title = this.titleMessage
                .map((message) -> replacer == null ? message : replacer.replace(commandSender, message))
                .orElse(null);
            final String subTitle = this.subTitleMessage
                .map((message) -> replacer == null ? message : replacer.replace(commandSender, message))
                .orElse(null);
            TitleSender.send(commandSender, this.titleTimes, title, subTitle);
        }
    }

    private void internalSendActionBar(CommandSender commandSender, Replacer replacer) {
        this.actionBarMessage
            .map((message) -> replacer == null ? message : replacer.replace(commandSender, message))
            .ifPresent((message) -> ActionBarSender.send(commandSender, message));
    }

    private Replacer resolveNullablePapiReplacer(Replacer replacer) {
        return replacer == null ? PlayerReplacer.createWithPapi() : replacer;
    }

}