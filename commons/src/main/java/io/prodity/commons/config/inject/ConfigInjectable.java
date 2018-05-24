package io.prodity.commons.config.inject;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Represents an object that can be injected byWithType a {@link ConfigInjector}.
 */
@FunctionalInterface
public interface ConfigInjectable {

    /**
     * Injects this object byWithType parsing the values from the specified {@link ConfigurationNode}
     *
     * @param context the {@link ConfigInjectionContext} of this injection
     * @param node the base node of the object being injected
     * @throws Throwable if the injection fails for said reason
     */
    void inject(ConfigInjectionContext context, ConfigurationNode node) throws Throwable;

}