package io.prodity.commons.spigot.legacy.config.yaml.types.enumeration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.collection.YamlList;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class YamlEnumList<E extends Enum<E>> extends AbstractYamlType<List> {

    private static final Map<Class<? extends Enum<?>>, YamlEnumList<?>> enumTypes = Maps.newHashMap();

    public static <T extends Enum<T>> YamlEnumList<T> get(Class<T> enumClass) {
        YamlEnumList<T> type = (YamlEnumList<T>) YamlEnumList.enumTypes.get(enumClass);
        if (type == null) {
            YamlEnumList.enumTypes.put(enumClass, type = new YamlEnumList<>(enumClass));
        }
        return type;
    }

    @Getter
    private final Class<E> enumClass;

    public YamlEnumList(Class<E> enumClass) {
        super(List.class, false);
        this.enumClass = enumClass;
    }

    @Override
    protected List<E> loadInternally(ConfigurationSection section, String path) throws YamlException {
        final List<String> strings = YamlList.get().load(section, path);
        final List<E> enums = Lists.newArrayList();

        final Set<String> failed = Sets.newHashSet();
        for (String string : strings) {
            try {
                final E value = Enum.valueOf(this.enumClass, string.toUpperCase());
                enums.add(value);
            } catch (IllegalArgumentException exception) {
                failed.add(string);
            }
        }
        if (!failed.isEmpty()) {
            final String failedString = String.join("," + failed);
            throw new YamlException(section, path,
                "values '" + failedString + "' are not valid types of '" + this.enumClass.getSimpleName() + "'");
        }

        return enums;
    }

}