package io.prodity.commons.config.inject;

import io.prodity.commons.config.inject.deserialize.ElementResolver;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Represents an object that returns a value required in the config injection process.
 *
 * @param <T> the type of value retured
 */
@FunctionalInterface
public interface ConfigResolvable<T> {

    @Nullable
    T resolve(ElementResolver elementResolver, ConfigurationNode node) throws Throwable;

}