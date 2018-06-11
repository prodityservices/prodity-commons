package io.prodity.commons.spigot.legacy.config.yaml.types.bukkit;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlSection extends AbstractYamlType<ConfigurationSection> {

    public static YamlSection get() {
        return YamlTypeCache.getType(YamlSection.class);
    }

    public YamlSection() {
        super(ConfigurationSection.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isConfigurationSection(path)) {
            throw new YamlException(section, path, "path is not a ConfigurationSection");
        }
    }

    @Override
    public ConfigurationSection loadInternally(ConfigurationSection section, String path) throws YamlException {
        return section.getConfigurationSection(path);
    }

}