package io.prodity.commons.spigot.legacy.message.repository;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlColorizedString;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.placeholder.PlaceholderHelper;
import java.io.File;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MessageRepository extends YamlRepository<String> {

    private final ActionBarHandler actionBarSender;
    private final ChatHandler chatSender;
    private final TitleHandler titleSender;

    public MessageRepository(Plugin plugin, String fileName) {
        super(plugin, fileName);
        this.actionBarSender = new ActionBarHandler(this);
        this.chatSender = new ChatHandler(this);
        this.titleSender = new TitleHandler(this);
    }

    public MessageRepository(File file) {
        super(file);
        this.actionBarSender = new ActionBarHandler(this);
        this.chatSender = new ChatHandler(this);
        this.titleSender = new TitleHandler(this);
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        return section.isString(path);
    }

    @Override
    protected String load(ConfigurationSection section, String path) throws YamlException {
        return YamlColorizedString.get().load(section, path);
    }

    private BaseComponent[] toComponents(String message) {
        if (message == null) {
            return new BaseComponent[0];
        }
        return TextComponent.fromLegacyText(message);
    }

    public String get(String key, Replacer replacer) {
        final String message = this.get(key);
        return replacer.replace(message);
    }

    public String get(String key, Replacer replacer, Player player) {
        final String message = this.get(key);
        return replacer.replace(player, message);
    }

    public String papiGet(String key, Player player) {
        final String message = this.get(key);
        if (message == null) {
            return null;
        }
        return PlaceholderHelper.setPlaceholders(player, message);
    }

    public String papiGet(String key, Replacer replacer, Player player) {
        final String message = this.get(key);
        if (message == null) {
            return null;
        }
        return replacer.replace(player, message);
    }

    public BaseComponent[] getAsComponents(String key) {
        final String message = this.get(key);
        return this.toComponents(message);
    }

    public BaseComponent[] getAsComponents(String key, Replacer replacer) {
        final String message = this.get(key, replacer);
        return this.toComponents(message);
    }

    public BaseComponent[] getAsComponents(String key, Replacer replacer, Player player) {
        final String message = this.get(key, replacer, player);
        return this.toComponents(message);
    }

    public BaseComponent[] papiGetAsComponenets(String key, Player player) {
        final String message = this.papiGet(key, player);
        return this.toComponents(message);
    }

    public BaseComponent[] papiGetAsComponenets(String key, Replacer replacer, Player player) {
        final String message = this.papiGet(key, replacer, player);
        return this.toComponents(message);
    }

    public ChatHandler chat() {
        return this.chatSender;
    }

    public ActionBarHandler actionBar() {
        return this.actionBarSender;
    }

    public TitleHandler title() {
        return this.titleSender;
    }

}