package io.prodity.commons.config.inject.deserialize;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigDeserializers {

    private static final ConfigDeserializerRegistry DEFAULT_REGISTRY = new ConfigDeserializerRegistry();

    static {
        ConfigDeserializers.DEFAULT_REGISTRY.register(ConfigDeserializers.STRING);
    }

    public static ConfigDeserializerRegistry getDefaultRegistry() {
        return ConfigDeserializers.DEFAULT_REGISTRY;
    }

    public static ConfigDeserializerRegistry createRegistry() {
        return new ConfigDeserializerRegistry(ConfigDeserializers.DEFAULT_REGISTRY);
    }

    public static final class StringDeserializer extends ConfigDeserializer<String> {

        @Override
        public String deserialize(ConfigurationNode node) throws Throwable {
            return null;
        }
        
    }

    public static final ConfigDeserializer<String> STRING = ConfigDeserializer
        .create(TypeToken.of(String.class), (node) -> node.getString());

}