package io.prodity.commons.spigot.legacy.config.yaml.types.collection;

import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

public class YamlSet extends AbstractYamlType<Set> {

    public static YamlSet get() {
        return YamlTypeCache.getType(YamlSet.class);
    }

    public YamlSet() {
        super(Set.class, false);
    }

    @Override
    public Set loadInternally(ConfigurationSection section, String path) throws YamlException {
        final List list = YamlList.get().load(section, path);
        return Sets.newHashSet(list);
    }

}