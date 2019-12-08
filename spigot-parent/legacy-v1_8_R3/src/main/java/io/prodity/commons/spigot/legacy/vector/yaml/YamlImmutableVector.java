package io.prodity.commons.spigot.legacy.vector.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.vector.ImmutableVector;
import org.bukkit.configuration.ConfigurationSection;

public class YamlImmutableVector extends AbstractYamlType<ImmutableVector> {

    public static YamlImmutableVector get() {
        return YamlTypeCache.getType(YamlImmutableVector.class);
    }

    public YamlImmutableVector() {
        super(ImmutableVector.class, false);
    }

    @Override
    public ImmutableVector loadInternally(ConfigurationSection section, String path) throws YamlException {
        return new ImmutableVector(YamlVector.get().load(section, path));
    }

}