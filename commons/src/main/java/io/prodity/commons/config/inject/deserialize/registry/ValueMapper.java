package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public class ValueMapper<T> extends ElementMapper<T, ValueMapper<T>> {

    /**
     * Specifies which {@link TypeToken} is supplied to the {@link ElementDeserializer}.
     */
    public enum TypeStrategy {

        /**
         * The type used is the type supplied to the {@link ElementDeserializer#deserialize(ConfigInjectionContext, TypeToken, ConfigurationNode)}
         */
        DESERIALIZED,

        /**
         * The type used is the type the value is being mapped from.
         */
        MAP_FROM,

        /**
         * The type used is the type the value is being mapped to.
         */
        MAP_TO

    }

    public ValueMapper(ElementDeserializerRegistry deserializerRegistry, TypeToken<T> typeToMap) {
        super(deserializerRegistry, typeToMap);
    }

    /**
     * Sets the type values are mapped from.
     *
     * @param typeToMapFrom the {@link TypeToken} of the type to mape values from
     * @param <R> the type to map values from
     * @return the {@link PreparedValueMapper} used to complete the mapping process
     */
    public <R> PreparedValueMapper<T, R> from(TypeToken<R> typeToMapFrom) {
        Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
        return new PreparedValueMapper<>(this, typeToMapFrom);
    }

    /**
     * Sets the type values are mapped from.
     *
     * @param typeToMapFrom the {@link Class} of the type to mape values from
     * @param <R> the type to map values from
     * @return the {@link PreparedValueMapper} used to complete the mapping process
     */
    public <R> PreparedValueMapper<T, R> from(Class<R> typeToMapFrom) {
        Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
        return new PreparedValueMapper<>(this, TypeToken.of(typeToMapFrom));
    }

    public static class PreparedValueMapper<T, R> {

        private final ValueMapper<T> valueMapper;
        private final TypeToken<R> typeToMapFrom;

        private TypeStrategy typeStrategy;
        private Function<TypeToken<?>, TypeToken<?>> tokenConverter;

        private PreparedValueMapper(ValueMapper<T> valueMapper, TypeToken<R> typeToMapFrom) {
            Preconditions.checkNotNull(valueMapper, "valueMapper");
            Preconditions.checkNotNull(typeToMapFrom, "typeToMapFrom");
            this.valueMapper = valueMapper;
            this.typeToMapFrom = typeToMapFrom;
        }

        /**
         * Sets the {@link TypeStrategy} that is used when determining which type should be specified to
         * {@link ElementDeserializer#deserialize(ConfigInjectionContext, TypeToken, ConfigurationNode)}.<br>
         * The default strategy is {@link TypeStrategy#DESERIALIZED}.
         *
         * @param typeStrategy the {@link TypeStrategy} to use
         * @return this {@link PreparedValueMapper} instance
         */
        public PreparedValueMapper<T, R> convertTypes(TypeStrategy typeStrategy) {
            Preconditions.checkNotNull(typeStrategy, "typeStrategy");
            if (this.tokenConverter != null) {
                throw new IllegalStateException("a tokenConverter has already been specified for PreparedValueMapper=" + this.toString());
            }

            this.typeStrategy = typeStrategy;
            return this;
        }

        /**
         * Executes {@link PreparedValueMapper#convertTypes(Function)} using a constant {@link TypeToken} as the return value.
         *
         * @param typeToken the {@link TypeToken}
         * @return this {@link PreparedValueMapper} instance
         */
        public PreparedValueMapper<T, R> convertTypes(TypeToken<R> typeToken) {
            return this.convertTypes((t) -> typeToken);
        }

        /**
         * Sets the {@link Function} used to convert the {@link TypeToken} of the type being deserialized to the
         * {@link TypeToken} of {@link PreparedValueMapper#typeToMapFrom}.<br>
         * This is primarily useful when the type has generic arguments that can not be resolved from the typeToMapFrom.
         *
         * @param tokenConverter the {@link Function} to convert the token being deserialized
         * @return this {@link PreparedValueMapper} instance
         */
        public PreparedValueMapper<T, R> convertTypes(Function<TypeToken<?>, TypeToken<?>> tokenConverter) {
            Preconditions.checkNotNull(tokenConverter, "tokenConverter");
            if (this.typeStrategy != null) {
                throw new IllegalStateException("a typeStrategy has already been specified for PreparedValueMapper=" + this.toString());
            }
            this.tokenConverter = tokenConverter;
            return this;
        }

        /**
         * Completes the value mapping process byWithType setting the {@link Function} used to convert values
         * of the <i>type to map from</i> to the <i>type to map to</i>.<br>
         * Registers a new {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer} that does the above.
         *
         * @param valueConverter the {@link Function} to convert the values
         */
        public void by(Function<R, T> valueConverter) {
            this.byWithType((token, value) -> valueConverter.apply(value));
        }

        /**
         * Completes the value mapping process byWithType setting the {@link Function} used to convert values
         * of the <i>type to map from</i> to the <i>type to map to</i>.<br>
         * Registers a new {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer} that does the above.
         *
         * @param valueConverter the {@link BiFunction} to convert the values, with the specified {@link TypeToken} argument always being
         * the type that is being deserialized.
         */
        public void byWithType(BiFunction<TypeToken<?>, R, T> valueConverter) {
            Preconditions.checkNotNull(valueConverter, "valueConverter");

            final ElementDeserializer<T> deserializer = new ElementDeserializer<T>(this.valueMapper.getPriority()) {
                @Override
                public boolean canDeserialize(TypeToken<?> type) {
                    return PreparedValueMapper.this.valueMapper.getStrategy().test(type);
                }

                @Nullable
                @Override
                public T deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
                    final TypeToken<R> typeToMapFrom = PreparedValueMapper.this.typeToMapFrom;

                    final ElementDeserializer<? extends R> typeDeserializer = PreparedValueMapper.this.valueMapper.getDeserializerRegistry()
                        .get(typeToMapFrom);

                    final TypeToken<?> typeToDeserialize;
                    if (PreparedValueMapper.this.tokenConverter != null) {
                        typeToDeserialize = PreparedValueMapper.this.tokenConverter.apply(type);
                    } else {
                        final TypeStrategy typeStrategy = PreparedValueMapper.this.typeStrategy;
                        switch (typeStrategy == null ? TypeStrategy.DESERIALIZED : typeStrategy) {
                            case MAP_TO:
                                typeToDeserialize = PreparedValueMapper.this.valueMapper.getTypeToMap();
                                break;
                            case MAP_FROM:
                                typeToDeserialize = typeToMapFrom;
                                break;
                            default:
                                typeToDeserialize = type;
                        }
                    }

                    final R value = typeDeserializer.deserialize(context, typeToDeserialize, node);
                    return valueConverter.apply(type, value);
                }
            };

            this.valueMapper.getDeserializerRegistry().register(deserializer);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("valueMapper", this.valueMapper)
                .add("typeToMapFrom", this.typeToMapFrom)
                .toString();
        }

    }

}