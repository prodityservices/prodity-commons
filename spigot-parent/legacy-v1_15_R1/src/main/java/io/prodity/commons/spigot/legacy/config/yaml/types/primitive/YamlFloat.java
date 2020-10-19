package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlFloat extends AbstractYamlType<Float> {

    public static YamlFloat get() {
        return YamlTypeCache.getType(YamlFloat.class);
    }

    public YamlFloat() {
        super(Float.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isInt(path) && !section.isDouble(path)) {
            throw new YamlException(section, path, "path is not a valid float (number)");
        }
    }

    @Override
    public Float loadInternally(ConfigurationSection section, String path) throws YamlException {
        if (section.isInt(path)) {
            return (float) section.getInt(path);
        }
        return (float) section.getDouble(path);
    }

}