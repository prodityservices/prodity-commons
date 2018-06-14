package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Maps a type's {@link ElementDeserializer} to another type's.
 *
 * @param <T> type to map
 */
public class TypeMapper<T> extends ElementMapper<T, TypeMapper<T>> {

    public TypeMapper(ElementDeserializerRegistry deserializerRegistry, TypeToken<T> typeToMap) {
        super(deserializerRegistry, typeToMap);
    }

    /**
     * Sets the type to map the value to and registers a new {@link ElementDeserializer} for the original type to map from
     *
     * @param mapTo the {@link TypeToken} of the type to map to
     * @param <T1> the type to map to
     */
    public <T1 extends T> void to(TypeToken<T1> mapTo) {
        Preconditions.checkNotNull(mapTo, "mapTo");

        final ElementDeserializer<T> deserializer = new ElementDeserializer<T>(this.priority) {
            @Override
            public boolean canDeserialize(TypeToken<?> type) {
                return TypeMapper.this.getStrategy().test(type);
            }

            @Nullable
            @Override
            public T deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
                return TypeMapper.this.getDeserializerRegistry().get(mapTo).deserialize(context, mapTo, node);
            }
        };

        this.getDeserializerRegistry().register(deserializer);
    }

    public <T1 extends T> void to(Class<T1> type) {
        Preconditions.checkNotNull(type, "type");
        this.to(TypeToken.of(type));
    }

}