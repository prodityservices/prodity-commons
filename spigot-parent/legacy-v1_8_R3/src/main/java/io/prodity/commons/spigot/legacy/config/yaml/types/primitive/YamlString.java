package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlString extends AbstractYamlType<String> {

    public static YamlString get() {
        return YamlTypeCache.getType(YamlString.class);
    }

    public YamlString() {
        super(String.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isString(path)) {
            throw new YamlException(section, path, "path is not a valid String");
        }
    }

    @Override
    public String loadInternally(ConfigurationSection section, String path) throws YamlException {
        return section.getString(path);
    }

}
