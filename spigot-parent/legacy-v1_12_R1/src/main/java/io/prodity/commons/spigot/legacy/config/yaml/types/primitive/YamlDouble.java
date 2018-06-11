package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlDouble extends AbstractYamlType<Double> {

    public static YamlDouble get() {
        return YamlTypeCache.getType(YamlDouble.class);
    }

    public YamlDouble() {
        super(Double.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isInt(path) && !section.isDouble(path)) {
            throw new YamlException(section, path, "path is not a valid double (number)");
        }
    }

    @Override
    public Double loadInternally(ConfigurationSection section, String path) throws YamlException {
        if (section.isInt(path)) {
            return (double) section.getInt(path);
        }
        return section.getDouble(path);
    }

}