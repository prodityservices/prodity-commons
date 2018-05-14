package io.prodity.commons.config.inject.member;

import io.prodity.commons.config.annotate.deserialize.Colorize.ColorizeState;
import io.prodity.commons.config.inject.ConfigInjectable;
import io.prodity.commons.config.inject.object.ConfigObject;
import java.util.Optional;

public interface ConfigMember extends ConfigInjectable {

    /**
     * Internal utility method to determine whether or not a {@link ConfigMember} should be colorized based on its defined {@link
     * io.prodity.commons.config.annotate.deserialize.Colorize} attribute and/or its possessor's attribute.
     */
    @Deprecated
    static <V> Optional<V> isColorized(Optional<V> superValue, boolean isPossessorColorized) {
        return superValue
            .map((state) -> {
                if (state == ColorizeState.TRUE) {
                    return state;
                }

                return state == ColorizeState.UNDEFINED && isPossessorColorized ? ColorizeState.TRUE : state;
            })
            .map((value) -> (V) value);
    }

    ConfigObject<?> getPossessor();

}