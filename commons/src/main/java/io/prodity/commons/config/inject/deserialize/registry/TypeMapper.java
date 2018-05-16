package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers;
import io.prodity.commons.config.inject.deserialize.TypeElementDeserializer;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Maps a type's {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer} to another type's.
 *
 * @param <T> type to map
 */
public class TypeMapper<T> {

    private final ElementDeserializerRegistry deserializerRegistry;
    private final TypeToken<T> typeToMap;
    private int priority = ElementDeserializers.DEFAULT_PRIORITY + 1;

    public TypeMapper(ElementDeserializerRegistry deserializerRegistry, TypeToken<T> typeToMap) {
        Preconditions.checkNotNull(deserializerRegistry, "deserializerRegistry");
        Preconditions.checkNotNull(typeToMap, "typeToMap");
        this.deserializerRegistry = deserializerRegistry;
        this.typeToMap = typeToMap;
    }

    /**
     * Sets the priority of the created {@link ElementDeserializer}. <br>
     * Default priority is {@link ElementDeserializers#DEFAULT_PRIORITY}+1
     *
     * @param priority the priority to set
     * @return this {@link TypeMapper} instance
     */
    public TypeMapper<T> withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Sets the type to map the value to and registers a new {@link ElementDeserializer} for the original type to map from
     *
     * @param mapTo the {@link TypeToken} of the type to map to
     * @param <T1> the type to map to
     */
    public <T1 extends T> void to(TypeToken<T1> mapTo) {
        Preconditions.checkNotNull(mapTo, "mapTo");

        final ElementDeserializer<T> deserializer = new TypeElementDeserializer<T>(this.typeToMap, this.priority) {
            @Nullable
            @Override
            public T deserialize(TypeToken<?> type, ConfigurationNode node) throws Throwable {
                return TypeMapper.this.deserializerRegistry.get(mapTo).deserialize(type, node);
            }
        };

        this.deserializerRegistry.register(deserializer);
    }

    public <T1 extends T> void to(Class<T1> type) {
        Preconditions.checkNotNull(type, "type");
        this.to(TypeToken.of(type));
    }

}