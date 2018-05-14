package io.prodity.commons.config.annotate.inject;

import com.google.common.base.Preconditions;
import java.util.IdentityHashMap;
import java.util.Map;

public enum Configs {

    ;

    private static final Map<Class<?>, Config> CONFIGS = new IdentityHashMap<>();

    public static Config getConfig(Class<?> type) throws IllegalArgumentException {
        Preconditions.checkNotNull(type, "type");
        return Configs.CONFIGS.computeIfAbsent(type, Configs::computeConfig);
    }

    private static Config computeConfig(Class<?> type) throws IllegalArgumentException {
        final Config config = type.getAnnotation(Config.class);

        if (config == null) {
            throw new IllegalArgumentException("class=" + type.getName() + " does not have the @Config annotation");
        }

        return config;
    }

}