package io.prodity.commons.spigot.legacy.config.yaml.types.collection;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class YamlImmutableSet extends AbstractYamlType<ImmutableSet> {

    public static YamlImmutableSet get() {
        return YamlTypeCache.getType(YamlImmutableSet.class);
    }

    public YamlImmutableSet() {
        super(ImmutableSet.class, false);
    }

    @Override
    public ImmutableSet loadInternally(ConfigurationSection section, String path) throws YamlException {
        final List list = YamlList.get().load(section, path);
        return ImmutableSet.copyOf(list);
    }

}