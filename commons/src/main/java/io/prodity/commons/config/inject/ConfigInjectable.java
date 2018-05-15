package io.prodity.commons.config.inject;

import io.prodity.commons.config.inject.deserialize.ElementResolver;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Represents an object that can be injected by a {@link ConfigInjector}.
 */
@FunctionalInterface
public interface ConfigInjectable {

    /**
     * Injects this object by parsing the values from the specified {@link ConfigurationNode}
     *
     * @param elementResolver the {@link ElementResolver} used to resolve the values
     * @param node the base node of the object being injected
     * @throws Throwable if the injection fails for said reason
     */
    void inject(ElementResolver elementResolver, ConfigurationNode node) throws Throwable;

}