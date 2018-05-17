package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.deserialize.TypeElementDeserializer;
import java.util.function.Function;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public class ValueMapper<T> {

    private final ElementDeserializerRegistry deserializerRegistry;
    private final TypeToken<T> typeToMap;
    private int priority = ElementDeserializers.DEFAULT_PRIORITY + 1;

    public ValueMapper(ElementDeserializerRegistry deserializerRegistry, TypeToken<T> typeToMap) {
        Preconditions.checkNotNull(deserializerRegistry, "deserializerRegistry");
        Preconditions.checkNotNull(typeToMap, "typeToMap");
        this.deserializerRegistry = deserializerRegistry;
        this.typeToMap = typeToMap;
    }

    /**
     * Sets the priority of the created {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer}. <br>
     * Default priority is {@link ElementDeserializers#DEFAULT_PRIORITY}+1
     *
     * @param priority the priority to set
     * @return this {@link ValueMapper} instance
     */
    public ValueMapper<T> withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Sets the type values are mapped from.
     *
     * @param typeToMapFrom the {@link TypeToken} of the type to mape values from
     * @param <R> the type to map values from
     * @return the {@link PreparedValueMapper} used to complete the mapping process
     */
    public <R> PreparedValueMapper<R> from(TypeToken<R> typeToMapFrom) {
        Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
        return new PreparedValueMapper<>(typeToMapFrom);
    }

    /**
     * Sets the type values are mapped from.
     *
     * @param typeToMapFrom the {@link Class} of the type to mape values from
     * @param <R> the type to map values from
     * @return the {@link PreparedValueMapper} used to complete the mapping process
     */
    public <R> PreparedValueMapper<R> from(Class<R> typeToMapFrom) {
        Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
        return new PreparedValueMapper<>(TypeToken.of(typeToMapFrom));
    }

    public class PreparedValueMapper<R> {

        private final TypeToken<R> typeToMapFrom;

        private PreparedValueMapper(TypeToken<R> typeToMapFrom) {
            Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
            this.typeToMapFrom = typeToMapFrom;
        }

        /**
         * Completes the value mapping process by setting the {@link Function} used to convert values
         * of the <i>type to map from</i> to the <i>type to map to</i>.<br>
         * Registers a new {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer} that does the above.
         *
         * @param valueConverter the function
         */
        public void by(Function<R, T> valueConverter) {
            Preconditions.checkNotNull(valueConverter, "valueConverter");

            final TypeToken<T> typeToMap = ValueMapper.this.typeToMap;
            final int priority = ValueMapper.this.priority;

            final ElementDeserializer<T> deserializer = new TypeElementDeserializer<T>(typeToMap, priority) {
                @Nullable
                @Override
                public T deserialize(ElementResolver resolver, TypeToken<?> type, ConfigurationNode node) throws Throwable {
                    final TypeToken<R> typeToMapFrom = PreparedValueMapper.this.typeToMapFrom;

                    final ElementDeserializer<? extends R> typeDeserializer = ValueMapper.this.deserializerRegistry.get(typeToMapFrom);
                    final R value = typeDeserializer.deserialize(resolver, typeToMapFrom, node);
                    return valueConverter.apply(value);
                }
            };

            ValueMapper.this.deserializerRegistry.register(deserializer);
        }

    }


}