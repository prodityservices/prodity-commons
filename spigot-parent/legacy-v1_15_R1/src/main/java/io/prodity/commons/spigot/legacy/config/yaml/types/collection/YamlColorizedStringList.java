package io.prodity.commons.spigot.legacy.config.yaml.types.collection;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class YamlColorizedStringList extends YamlList {

    public static YamlColorizedStringList get() {
        return YamlTypeCache.getType(YamlColorizedStringList.class);
    }

    public YamlColorizedStringList() {
        super();
    }

    @Override
    public List<String> loadInternally(ConfigurationSection section, String path) throws YamlException {
        final List<String> strings = super.loadInternally(section, path);
        for (int i = 0; i < strings.size(); i++) {
            final String string = strings.get(i);
            strings.set(i, ChatColor.translateAlternateColorCodes('&', string));
        }
        return strings;
    }

}
