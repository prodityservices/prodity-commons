package io.prodity.commons.config.inject.element;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Strategies defining how the values of {@link ConfigElement}s should be resolved.
 */
public enum ElementInjectionStrategy {

    /**
     * The value is resolved from a path of a {@link ConfigurationNode}.
     */
    NODE_PATH,

    /**
     * The value is resolved from the key of a {@link ConfigurationNode}.
     */
    NODE_KEY

}