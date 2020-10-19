package io.prodity.commons.spigot.legacy.config.yaml;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnum;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnumList;
import io.prodity.commons.spigot.legacy.tryto.Try;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class YamlTypeCache {

    private static final Map<Class<?>, YamlType<?>> TYPE_MAP = Maps.newHashMap();
    private static final Map<Class<? extends Enum<?>>, YamlEnum<?>> ENUM_MAP = Maps.newHashMap();
    private static final Map<Class<? extends Enum<?>>, YamlEnumList<?>> ENUM_LIST_MAP = Maps.newHashMap();

    public static <T extends YamlType<R>, R> T createType(Class<T> clazz) {
        return Try.get(clazz::newInstance);
    }

    public static <T extends YamlType<R>, R> T getType(Class<T> clazz) {
        if (!YamlTypeCache.TYPE_MAP.containsKey(clazz)) {
            final T value = YamlTypeCache.createType(clazz);
            YamlTypeCache.TYPE_MAP.put(clazz, value);
        }
        return (T) YamlTypeCache.TYPE_MAP.get(clazz);
    }

    public static <E extends Enum<E>, T extends YamlEnum<E>> T createEnumType(Class<E> enumClazz) {
        return (T) new YamlEnum<>(enumClazz);
    }

    public static <T extends YamlEnumList<E>, E extends Enum<E>> T createEnumListType(Class<E> enumClazz) {
        return (T) new YamlEnumList<>(enumClazz);
    }

    public static <T extends YamlEnum<E>, E extends Enum<E>> T getEnumType(Class<E> enumClazz) {
        if (!YamlTypeCache.ENUM_MAP.containsKey(enumClazz)) {
            final T value = YamlTypeCache.createEnumType(enumClazz);
            YamlTypeCache.ENUM_MAP.put(enumClazz, value);
        }
        return (T) YamlTypeCache.ENUM_MAP.get(enumClazz);
    }

    public static <T extends YamlEnumList<E>, E extends Enum<E>> T getEnumListType(Class<E> enumClazz) {
        if (!YamlTypeCache.ENUM_LIST_MAP.containsKey(enumClazz)) {
            final T value = YamlTypeCache.createEnumListType(enumClazz);
            YamlTypeCache.ENUM_LIST_MAP.put(enumClazz, value);
        }
        return (T) YamlTypeCache.ENUM_LIST_MAP.get(enumClazz);
    }

}