package io.prodity.commons.spigot.legacy.config.yaml;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import org.bukkit.configuration.ConfigurationSection;

public class YamlException extends Exception {

    private final String path;
    private final String causeMessage;

    private final LazyValue<String> message = new SimpleLazyValue<>(this::createMessage);

    public YamlException(ConfigurationSection section, String message) {
        super(message);
        this.path = section.getCurrentPath();
        this.causeMessage = message;
    }

    public YamlException(ConfigurationSection section, String path, String message) {
        super(message);
        this.path = YamlConfig.getFullPath(section, path);
        this.causeMessage = message;
    }

    private String createMessage() {
        return "error while loading Yaml value @ '" + this.path + "': " + this.causeMessage;
    }

    @Override
    public String getMessage() {
        return this.message.get();
    }

}