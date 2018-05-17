package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.color.Color;
import io.prodity.commons.color.ImmutableColor;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.NumberSerializer;
import io.prodity.commons.inject.Export;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.annotation.PostConstruct;
import org.jvnet.hk2.annotations.Service;

/***
 * A globally shared registry for {@link ElementDeserializer}s.
 */
@Export
@Service
public class ElementDeserializerRegistry {

    private final Set<ElementDeserializer<?>> deserializers = new ConcurrentSkipListSet<>(Comparator.reverseOrder());

    @PostConstruct
    private void registerDefaults() {
        this.register(new NumberSerializer());

        this.mapType(Color.class).to(ImmutableColor.class);
    }

    public ElementDeserializerRegistry registerAll(ElementDeserializerRegistry registry) {
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.addAll(registry.deserializers);
        return this;
    }

    public ElementDeserializerRegistry register(ElementDeserializer<?> deserializer) {
        Preconditions.checkNotNull(deserializer, "deserializer");
        this.deserializers.add(deserializer);
        return this;
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

        for (ElementDeserializer<?> deserializer : this.deserializers) {
            if (deserializer.canDeserialize(type)) {
                return (ElementDeserializer<? extends T>) deserializer;
            }
        }

        throw new IllegalArgumentException("no ElementDeserializer registered for type=" + type.toString());
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