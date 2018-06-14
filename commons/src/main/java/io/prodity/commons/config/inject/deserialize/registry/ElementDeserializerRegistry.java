package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.color.Color;
import io.prodity.commons.color.ImmutableColor;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.BooleanDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.ConfigObjectDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.EnumValueDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.ListDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.MapDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.NumberSerializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.PatternDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.StringDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.UUIDDeserializer;
import io.prodity.commons.config.inject.deserialize.registry.ElementMapper.MapperStrategy;
import io.prodity.commons.inject.Export;
import io.prodity.commons.reflect.TypeTokens;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.glassfish.hk2.api.IterableProvider;
import org.jvnet.hk2.annotations.Service;

/***
 * A globally shared registry for {@link ElementDeserializer}s.
 */
@Export
@Service
public class ElementDeserializerRegistry {

    private final List<ElementDeserializer<?>> deserializers = Lists.newCopyOnWriteArrayList();

    @Inject
    private IterableProvider<ElementDeserializerModifier> modifiers;

    @PostConstruct
    private void registerDefaults() {
        this.register(new NumberSerializer());
        this.register(new ConfigObjectDeserializer());
        this.register(new StringDeserializer());
        this.register(new BooleanDeserializer());
        this.register(new EnumValueDeserializer());
        this.register(new UUIDDeserializer());
        this.register(new PatternDeserializer());
        this.register(new ListDeserializer());
        this.register(new MapDeserializer());

        this.mapType(Color.class)
            .to(ImmutableColor.class);

        this.mapValueOf(new TypeToken<Object[]>() {})
            .withStrategy((typeToMap, type) -> type.isArray())
            .withPriority(ElementDeserializers.DEFAULT_PRIORITY + 2)
            .from(new TypeToken<List<Object>>() {})
            .convertTypes((type) -> TypeTokens.listToken(type.getComponentType()))
            .byWithType((token, value) -> {
                final Object[] array = (Object[]) Array.newInstance((Class<?>) token.getComponentType().getType(), value.size());
                for (int index = 0; index < value.size(); index++) {
                    array[index] = value.get(index);
                }
                return array;
            });

        this.mapType(new TypeToken<Collection<?>>() {})
            .withStrategy(MapperStrategy.RAW_TYPE)
            .to(new TypeToken<List<?>>() {});

        this.mapValueOf(new TypeToken<Set<?>>() {})
            .withStrategy(MapperStrategy.RAW_TYPE)
            .from(new TypeToken<List<?>>() {})
            .by(Sets::newHashSet);

        this.mapValueOf(new TypeToken<ImmutableList<?>>() {})
            .withStrategy(MapperStrategy.RAW_TYPE)
            .from(new TypeToken<List<?>>() {})
            .by(ImmutableList::copyOf);

        this.mapValueOf(new TypeToken<ImmutableSet<?>>() {})
            .withStrategy(MapperStrategy.RAW_TYPE)
            .from(new TypeToken<List<?>>() {})
            .by(ImmutableSet::copyOf);

        this.mapValueOf(new TypeToken<ImmutableMap<?, ?>>() {})
            .withStrategy(MapperStrategy.RAW_TYPE)
            .from(new TypeToken<Map<?, ?>>() {})
            .by(ImmutableMap::copyOf);

        this.mapValueOf(new TypeToken<ConcurrentMap<?, ?>>() {})
            .withStrategy(MapperStrategy.SUB_TYPE)
            .from(new TypeToken<Map<?, ?>>() {})
            .by(ConcurrentHashMap::new);

        this.mapValueOf(LevenshteinDistance.class).from(Integer.class).by(LevenshteinDistance::new);

        this.modifiers.forEach((modifier) -> modifier.modify(this));
    }

    public ElementDeserializerRegistry registerAll(ElementDeserializerRegistry registry) {
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.addAll(registry.deserializers);
        this.sortDeserializers();
        return this;
    }

    public ElementDeserializerRegistry register(ElementDeserializer<?> deserializer) {
        Preconditions.checkNotNull(deserializer, "deserializer");
        this.deserializers.add(deserializer);
        this.sortDeserializers();
        return this;
    }

    private void sortDeserializers() {
        Collections.sort(this.deserializers, Comparator.reverseOrder());
    }

    /**
     * Gets the {@link ElementDeserializer} for the specified {@link TypeToken} if present
     *
     * @param type the {@link TypeToken} of the type
     * @param <T> the type
     * @return the {@link ElementDeserializer}
     * @throws IllegalArgumentException if there is no registered {@link ElementDeserializer} for the specified type
     */
    public <T> ElementDeserializer<? extends T> get(TypeToken<T> type) throws IllegalArgumentException {
        Preconditions.checkNotNull(type, "type");

        final TypeToken<T> wrappedType = type.wrap();

        for (ElementDeserializer<?> deserializer : this.deserializers) {
            if (deserializer.canDeserialize(wrappedType)) {
                return (ElementDeserializer<? extends T>) deserializer;
            }
        }

        throw new IllegalArgumentException(
            "no ElementDeserializer registered for type=" + type + " (wrapped=" + wrappedType + ")");
    }

    /**
     * Starts a type mapping process to map the specified type to a child type.
     *
     * @param type the {@link TypeToken} of the type
     * @param <T> the type
     * @return the {@link TypeMapper} used to complete the process
     */
    public <T> TypeMapper<T> mapType(TypeToken<T> type) {
        Preconditions.checkNotNull(type, "type");
        return new TypeMapper<>(this, type);
    }

    /**
     * Starts a type mapping process to map the specified type
     *
     * @param type the {@link Class} of the type
     * @param <T> the type
     * @return the {@link TypeMapper} used to complete the process
     */
    public <T> TypeMapper<T> mapType(Class<T> type) {
        Preconditions.checkNotNull(type, "type");
        return this.mapType(TypeToken.of(type));
    }

    /**
     * Starts a value mapping process that deserializes values of the specified type from another type
     *
     * @param type the {@link TypeToken} of the type values are to be mapped to
     * @param <T> the type
     * @return the {@link ValueMapper} used to continue the process
     */
    public <T> ValueMapper<T> mapValueOf(TypeToken<T> type) {
        Preconditions.checkNotNull(type, "type");
        return new ValueMapper<>(this, type);
    }

    /**
     * Starts a value mapping process that deserializes values of the specified type from another type
     *
     * @param type the {@link Class} of the type values are to be mapped to
     * @param <T> the type
     * @return the {@link ValueMapper} used to continue the process
     */
    public <T> ValueMapper<T> mapValueOf(Class<T> type) {
        Preconditions.checkNotNull(type, "type");
        return this.mapValueOf(TypeToken.of(type));
    }

}