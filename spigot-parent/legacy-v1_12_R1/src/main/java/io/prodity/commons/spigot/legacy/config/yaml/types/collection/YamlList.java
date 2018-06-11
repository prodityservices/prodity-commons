package io.prodity.commons.spigot.legacy.config.yaml.types.collection;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class YamlList extends AbstractYamlType<List> {

    public static YamlList get() {
        return YamlTypeCache.getType(YamlList.class);
    }

    public YamlList() {
        super(List.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        if (!section.isList(path)) {
            throw new YamlException(section, path, "path is not a List");
        }
    }

    @Override
    public List loadInternally(ConfigurationSection section, String path) throws YamlException {
        return section.getList(path);
    }

}