package io.prodity.commons.spigot.legacy.version;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.tryto.Try;
import org.bukkit.Bukkit;

import java.util.Map;

public enum Version {

    ;

    public static final String UTILITIES_PROVIDER_PACKAGE = "io.prodity.commons.spigot.legacy.version";
    private static final LazyValue<String> PACKAGE_NAME = new SimpleLazyValue<>(Version::findPackageName);

    private static final Map<Class<?>, VersionProvider> PROVIDERS = Maps.newHashMap();

    private static String findPackageName() {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.split("\\.")[3];
    }

    private static String getPackageName() {
        return Version.PACKAGE_NAME.get();
    }

    public static <T extends VersionProvider> T createProvider(String providerClassName)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        final Class<?> clazz = Class.forName(providerClassName);
        return (T) clazz.newInstance();
    }

    public static <T extends VersionProvider> T getUtilitiesProvider(Class<T> providerClazz, String providerClassName) {
        return Version.getProvider(providerClazz, Version.UTILITIES_PROVIDER_PACKAGE, providerClassName);
    }

    public static <T extends VersionProvider> T getProvider(Class<T> providerClazz, String providersPackage, String providerClassName) {
        final String name = String.join(".", providersPackage, Version.getPackageName(), providerClassName);

        if (!Version.PROVIDERS.containsKey(providerClazz)) {
            final T provider = Try.get(() -> Version.createProvider(name));
            if (provider == null) {
                throw new NullPointerException("could not load provider '" + name + "'");
            }
            Version.PROVIDERS.put(providerClazz, provider);
        }

        return (T) Version.PROVIDERS.get(providerClazz);
    }

}