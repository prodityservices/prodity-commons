package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class YamlColorizedString extends YamlString {

    public static YamlColorizedString get() {
        return YamlTypeCache.getType(YamlColorizedString.class);
    }

    public YamlColorizedString() {
        super();
    }

    @Override
    public String loadInternally(ConfigurationSection section, String path) throws YamlException {
        final String string = super.loadInternally(section, path);
        if (string == null) {
            return string;
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
