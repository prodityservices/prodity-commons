package io.prodity.commons.config.inject.deserialize;

import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Deserializes objects from a configuration.
 *
 * @param <T> The type to be deserialized
 */
public abstract class ElementDeserializer<T> implements Comparable<ElementDeserializer<?>> {

    private final int priority;

    public ElementDeserializer(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the priority. The greater the returned value, the greater the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Returns whether or not the specified {@link TypeToken} can be deserialized byWithType this {@link ElementDeserializer}.
     *
     * @param type the type
     * @return true if the specified element can be deserialized byWithType this instance, false if not
     */
    public abstract boolean canDeserialize(TypeToken<?> type);

    /**
     * Deserializes the specified {@link ConfigurationNode}.
     *
     * @param context the {@link ConfigInjectionContext} in use
     * @param type the {@link TypeToken} of the serialized type
     * @param node the {@link ConfigurationNode} to deserialize from
     * @return the deserialized object, possibly null
     * @throws Throwable if the deserialization fails
     */
    @Nullable
    public abstract T deserialize(ConfigInjectionContext context, TypeToken<?> type, ConfigurationNode node) throws Throwable;

    @Override
    public int compareTo(ElementDeserializer<?> other) {
        return Integer.compare(this.priority, other.priority);
    }

}