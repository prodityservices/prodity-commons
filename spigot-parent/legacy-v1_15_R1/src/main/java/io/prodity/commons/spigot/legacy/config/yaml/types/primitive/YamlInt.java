package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlInt extends AbstractYamlType<Integer> {

    public static YamlInt get() {
        return YamlTypeCache.getType(YamlInt.class);
    }

    public YamlInt() {
        super(Integer.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isInt(path)) {
            throw new YamlException(section, path, "path is not a valid integer");
        }
    }

    @Override
    public Integer loadInternally(ConfigurationSection section, String path) throws YamlException {
        return section.getInt(path);
    }

}