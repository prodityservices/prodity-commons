package io.prodity.commons.config.inject.deserialize;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public enum ElementDeserializers {

    ;

    /**
     * The default priority to used for the default serializers.
     */
    public static final int DEFAULT_PRIORITY = 1;
    public static final int LOW_PRIORITY = 10;
    public static final int MEDIUM_PRIORITY = 100;
    public static final int HIGH_PRIORITY = 1000;

    public static final class NumberSerializer extends TypeElementDeserializer<Number> {

        private static final ImmutableMap<Class<? extends Number>, Function<ConfigurationNode, Number>> NUMBER_TYPES =
            ImmutableMap.<Class<? extends Number>, Function<ConfigurationNode, Number>>builder()
                .put(AtomicInteger.class, (node) -> new AtomicInteger(node.getInt()))
                .put(AtomicLong.class, (node) -> new AtomicLong(node.getLong()))
                .put(BigDecimal.class, (node) -> new BigDecimal(node.getDouble()))
                .put(BigInteger.class, (node) -> BigInteger.valueOf(node.getLong()))
                .put(Byte.class, (node) -> (byte) node.getInt())
                .put(Double.class, ConfigurationNode::getDouble)
                .put(Float.class, ConfigurationNode::getFloat)
                .put(Integer.class, ConfigurationNode::getInt)
                .put(Long.class, ConfigurationNode::getLong)
                .put(Short.class, (node) -> (short) node.getInt())
                .build();

        public NumberSerializer() {
            super(TypeToken.of(Number.class), ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Nullable
        @Override
        public Number deserialize(TypeToken<?> type, ConfigurationNode node) {
            final TypeToken<?> wrappedType = type.wrap();
            final Class<?> typeClass = wrappedType.getRawType();

            if (!NumberSerializer.NUMBER_TYPES.containsKey(typeClass)) {
                throw new IllegalArgumentException("type=" + wrappedType + " is an unknown Number type");
            }

            return NumberSerializer.NUMBER_TYPES.get(typeClass).apply(node);
        }

    }

}