package io.prodity.commons.config.load.value;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class ConfigDeserializerRegistry {

    public static ConfigDeserializerRegistry create() {
        return new ConfigDeserializerRegistry();
    }

    private final Map<Class<?>, ConfigDeserializer<?>> deserializers;

    protected ConfigDeserializerRegistry() {
        this.deserializers = Maps.newConcurrentMap();
    }

    @Nonnull
    public ConfigDeserializerRegistry registerAll(@Nonnull ConfigDeserializerRegistry registry) {
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.putAll(registry.deserializers);
        return this;
    }

    @Nonnull
    public <T> ConfigDeserializerRegistry register(@Nonnull ConfigDeserializer<T> deserializer) {
        Preconditions.checkNotNull(deserializer, "deserializer");

        this.deserializers.put(deserializer.getTypeClass(), deserializer);
        return this;
    }

    @Nonnull
    public <T> Optional<ConfigDeserializer<T>> getDeserializer(@Nullable Class<T> clazz) {
        if (clazz == null || !this.deserializers.containsKey(clazz)) {
            return Optional.empty();
        }
        final ConfigDeserializer<T> deserializer = (ConfigDeserializer<T>) this.deserializers.get(clazz);
        return Optional.of(deserializer);
    }

    @Nonnull
    public <T> Optional<ConfigDeserializer<T>> unregister(@Nullable Class<T> clazz) {
        if (clazz == null || !this.deserializers.containsKey(clazz)) {
            return Optional.empty();
        }
        final ConfigDeserializer<T> removed = (ConfigDeserializer<T>) this.deserializers.remove(clazz);
        return Optional.of(removed);
    }

    public void clear() {
        this.deserializers.clear();
    }

}