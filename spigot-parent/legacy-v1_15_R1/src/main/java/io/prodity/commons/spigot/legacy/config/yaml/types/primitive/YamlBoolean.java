package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlBoolean extends AbstractYamlType<Boolean> {

    public static YamlBoolean get() {
        return YamlTypeCache.getType(YamlBoolean.class);
    }

    public YamlBoolean() {
        super(Boolean.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isBoolean(path)) {
            throw new YamlException(section, path, "path is not a valid boolean (true/false)");
        }
    }

    @Override
    public Boolean loadInternally(ConfigurationSection section, String path) throws YamlException {
        return section.getBoolean(path);
    }

}