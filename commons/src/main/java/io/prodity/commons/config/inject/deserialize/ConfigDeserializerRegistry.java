package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class ConfigDeserializerRegistry {

    private final Map<TypeToken<?>, ConfigDeserializer<?>> deserializers;

    public ConfigDeserializerRegistry() {
        this.deserializers = Maps.newConcurrentMap();
    }

    public ConfigDeserializerRegistry(ConfigDeserializerRegistry registry) {
        this();
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.putAll(registry.deserializers);
    }

    public ConfigDeserializerRegistry registerAll(ConfigDeserializerRegistry registry) {
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.putAll(registry.deserializers);
        return this;
    }

    public <T> ConfigDeserializerRegistry register(ConfigDeserializer<T> deserializer) {
        Preconditions.checkNotNull(deserializer, "deserializer");

        this.deserializers.put(deserializer.getType(), deserializer);
        return this;
    }

    public <T> Optional<ConfigDeserializer<T>> getDeserializer(@Nullable TypeToken<T> typeToken) {
        if (typeToken == null || !this.deserializers.containsKey(typeToken)) {
            return Optional.empty();
        }
        final ConfigDeserializer<T> deserializer = (ConfigDeserializer<T>) this.deserializers.get(typeToken);
        return Optional.of(deserializer);
    }

}