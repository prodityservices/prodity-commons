package io.prodity.commons.config.inject.deserialize;

import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.element.ConfigElement;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Deserializes objects from a configuration.
 *
 * @param <T> The type to be deserialized
 */
public interface ConfigDeserializer<T> {

    /**
     * Returns whether or not the specified {@link ConfigElement} can be deserialized by this {@link ConfigDeserializer}.
     *
     * @param element the element
     * @return true if the specified element can be deserialized by this instance, false if not
     */
    boolean canDeserialize(ConfigElement<T> element);

    /**
     * Deserializes the specified {@link ConfigurationNode}.
     *
     * @param typeToken the {@link TypeToken} containing the type to deserialize to
     * @param node the {@link ConfigurationNode} to deserialize from
     * @return the deserialized object, possibly null
     * @throws Throwable if the deserialization fails
     */
    @Nullable
    T deserialize(TypeToken<T> typeToken, ConfigurationNode node) throws Throwable;

}