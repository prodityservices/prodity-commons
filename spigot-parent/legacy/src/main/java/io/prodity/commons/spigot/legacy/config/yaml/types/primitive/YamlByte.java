package io.prodity.commons.spigot.legacy.config.yaml.types.primitive;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlByte extends AbstractYamlType<Byte> {

    public static YamlByte get() {
        return YamlTypeCache.getType(YamlByte.class);
    }

    public YamlByte() {
        super(Byte.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isInt(path)) {
            throw new YamlException(section, path, "path is not a valid byte (Integer)");
        }
    }

    @Override
    public Byte loadInternally(ConfigurationSection section, String path) throws YamlException {
        return (byte) section.getInt(path);
    }

}
