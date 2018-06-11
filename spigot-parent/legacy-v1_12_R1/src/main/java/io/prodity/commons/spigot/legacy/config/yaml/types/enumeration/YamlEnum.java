package io.prodity.commons.spigot.legacy.config.yaml.types.enumeration;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import java.util.Map;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

public class YamlEnum<E extends Enum<E>> extends AbstractYamlType<E> {

    private static final Map<Class<? extends Enum<?>>, YamlEnum<?>> enumTypes = Maps.newHashMap();

    public static <E extends Enum<E>> YamlEnum<E> get(Class<E> enumClass) {
        return YamlTypeCache.getEnumType(enumClass);
    }

    @Getter
    private final Class<E> enumClass;

    public YamlEnum(Class<E> enumClass) {
        super(enumClass, false);
        this.enumClass = enumClass;
    }

    @Override
    protected E loadInternally(ConfigurationSection section, String path) throws YamlException {
        final String string = YamlString.get().load(section, path);
        try {
            return Enum.valueOf(this.enumClass, string.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new YamlException(section, path,
                "value '" + string + "' is not valid element of '" + this.enumClass.getSimpleName() + "'");
        }
    }

}