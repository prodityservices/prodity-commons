package io.prodity.commons.config.inject.deserialize;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.deserialize.ConfigDeserializable;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.ConfigObject;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;

public enum ElementDeserializers {

    ;

    public static class NumberSerializer extends TypeElementDeserializer<Number> {

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
            super(TypeToken.of(Number.class), ElementDeserializers.DEFAULT_PRIORITY, true);
        }

        @Nullable
        @Override
        public Number deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            final TypeToken<?> wrappedType = type.wrap();
            final Class<?> typeClass = wrappedType.getRawType();

            if (!NumberSerializer.NUMBER_TYPES.containsKey(typeClass)) {
                throw new IllegalArgumentException("type=" + wrappedType + " is an unknown Number type");
            }

            return NumberSerializer.NUMBER_TYPES.get(typeClass).apply(node);
        }

    }

    public static class ConfigObjectDeserializer extends ElementDeserializer<Object> {

        public ConfigObjectDeserializer() {
            super(ElementDeserializers.DEFAULT_PRIORITY - 1);
        }

        @Override
        public boolean canDeserialize(TypeToken<?> type) {
            return type.getRawType().isAnnotationPresent(ConfigDeserializable.class);
        }

        @Nullable
        @Override
        public Object deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
            final ConfigObject<?> configObject = ConfigObject.of(type.getRawType());
            configObject.inject(context, node);
            return configObject.getObjectInstance();
        }

    }

    public static class StringDeserializer extends TypeElementDeserializer<String> {

        public StringDeserializer() {
            super(TypeToken.of(String.class), ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Override
        public String deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            return node.getString();
        }

    }

    public static class BooleanDeserializer extends TypeElementDeserializer<Boolean> {

        public BooleanDeserializer() {
            super(TypeToken.of(Boolean.class), ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Override
        public Boolean deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            return node.getBoolean();
        }

    }

    public static class EnumValueDeserializer extends TypeElementDeserializer<Enum> {

        public EnumValueDeserializer() {
            super(new TypeToken<Enum>() {}, ElementDeserializers.DEFAULT_PRIORITY, true);
        }

        @Nullable
        @Override
        public Enum deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            String enumValueName = node.getString();
            if (enumValueName == null) {
                throw new IllegalStateException("no value present when deserializing Enum value");
            }
            enumValueName = enumValueName.toUpperCase();

            final Class<? extends Enum> enumClass = type.getRawType().asSubclass(Enum.class);

            Enum<?> enumValue = null;
            for (Enum<?> field : enumClass.getEnumConstants()) {
                if (enumValueName.equals(field.name())) {
                    enumValue = field;
                    break;
                }
            }

            if (enumValue == null) {
                throw new IllegalStateException(
                    "invalid enum constant=" + enumValueName + " specified when deserializing enum type=" + enumClass.getName());
            }

            return enumValue;
        }

    }

    public static class UUIDDeserializer extends TypeElementDeserializer<UUID> {

        public UUIDDeserializer() {
            super(TypeToken.of(UUID.class), ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Nullable
        @Override
        public UUID deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            final String uuidString = node.getString();
            if (uuidString == null) {
                throw new IllegalStateException("no value present when deserializing UUID");
            }
            return UUID.fromString(uuidString);
        }

    }

    public static class PatternDeserializer extends TypeElementDeserializer<Pattern> {

        public PatternDeserializer() {
            super(TypeToken.of(Pattern.class), ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Nullable
        @Override
        public Pattern deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) {
            final String patternString = node.getString();
            if (patternString == null) {
                throw new IllegalStateException("no value present when deserializing Pattern");
            }
            return Pattern.compile(patternString);
        }

    }

    public static class ListDeserializer extends ElementDeserializer<List<?>> {

        private static final TypeToken<List<?>> LIST_TOKEN = new TypeToken<List<?>>() {};

        public ListDeserializer() {
            super(ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Override
        public boolean canDeserialize(TypeToken<?> type) {
            return ListDeserializer.LIST_TOKEN.isSupertypeOf(type) || List.class.isAssignableFrom(type.getRawType());
        }

        @Nullable
        @Override
        public List<?> deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
            if (!(type.getType() instanceof ParameterizedType)) {
                throw new IllegalStateException("raw types are not supported for Lists");
            }

            final TypeToken<?> entryToken = type.resolveType(Collection.class.getTypeParameters()[0]);
            final ElementDeserializer<?> elementDeserializer = context.getDeserializerRegistry().get(entryToken);

            if (node.hasListChildren() || node.hasMapChildren()) {
                final Collection<? extends ConfigurationNode> nodeChildren;
                if (node.hasListChildren()) {
                    nodeChildren = node.getChildrenList();
                } else {
                    nodeChildren = node.getChildrenMap().values();
                }

                final List<Object> list = Lists.newArrayList();

                for (ConfigurationNode childNode : nodeChildren) {
                    final Object value = elementDeserializer.deserialize(context, entryToken, childNode);
                    list.add(value);
                }

                return list;
            } else {
                final Object rawValue = node.getValue();
                if (rawValue != null) {
                    final Object value = elementDeserializer.deserialize(context, entryToken, node);
                    return Lists.newArrayList(value);
                }
            }

            return null;
        }

    }

    public static final class MapDeserializer extends ElementDeserializer<Map<?, ?>> {

        private static final TypeToken<Map<?, ?>> MAP_TOKEN = new TypeToken<Map<?, ?>>() {};

        public MapDeserializer() {
            super(ElementDeserializers.DEFAULT_PRIORITY);
        }

        @Override
        public boolean canDeserialize(TypeToken<?> type) {
            return MapDeserializer.MAP_TOKEN.isSupertypeOf(type) || Map.class.isAssignableFrom(type.getRawType());
        }

        @Nullable
        @Override
        public Map<?, ?> deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable {
            if (!node.hasMapChildren()) {
                return null;
            }

            if (!(type.getType() instanceof ParameterizedType)) {
                throw new IllegalStateException("raw types are not supported for Maps");
            }

            final Map<Object, Object> map = Maps.newLinkedHashMap();

            final ElementDeserializerRegistry deserializerRegistry = context.getDeserializerRegistry();

            final TypeToken<?> keyToken = type.resolveType(Map.class.getTypeParameters()[0]);
            final ElementDeserializer<?> keyDeserializer = deserializerRegistry.get(keyToken);

            final TypeToken<?> valueToken = type.resolveType(Map.class.getTypeParameters()[1]);
            final ElementDeserializer<?> valueDeserializer = deserializerRegistry.get(valueToken);

            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getChildrenMap().entrySet()) {
                final Object keyValue = keyDeserializer
                    .deserialize(context, keyToken, SimpleConfigurationNode.root().setValue(entry.getKey()));
                final Object valueValue = valueDeserializer.deserialize(context, valueToken, entry.getValue());

                if (keyValue == null || valueValue == null) {
                    continue;
                }

                map.put(keyValue, valueValue);
            }

            return map;
        }

    }

    /**
     * The default priority to used for the default serializers.
     */
    public static final int DEFAULT_PRIORITY = 1;
    public static final int LOW_PRIORITY = 10;
    public static final int MEDIUM_PRIORITY = 100;
    public static final int HIGH_PRIORITY = 1000;

    private static ConfigurationNode wrapObject(Object object) {
        return object instanceof ConfigurationNode ? (ConfigurationNode) object : SimpleConfigurationNode.root().setValue(object);
    }

}