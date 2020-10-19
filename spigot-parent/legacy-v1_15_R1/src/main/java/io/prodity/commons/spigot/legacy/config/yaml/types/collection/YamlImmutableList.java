package io.prodity.commons.spigot.legacy.config.yaml.types.collection;

import com.google.common.collect.ImmutableList;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import org.bukkit.configuration.ConfigurationSection;

public class YamlImmutableList extends AbstractYamlType<ImmutableList> {

    public static YamlImmutableList get() {
        return YamlTypeCache.getType(YamlImmutableList.class);
    }

    public YamlImmutableList() {
        super(ImmutableList.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isList(path)) {
            throw new YamlException(section, path, "path is not a List");
        }
    }

    @Override
    public ImmutableList loadInternally(ConfigurationSection section, String path) throws YamlException {
        return ImmutableList.copyOf(section.getList(path));
    }

}