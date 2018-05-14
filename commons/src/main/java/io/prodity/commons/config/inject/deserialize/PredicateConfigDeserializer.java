package io.prodity.commons.config.inject.deserialize;

import io.prodity.commons.config.inject.element.ConfigElement;
import java.util.function.Predicate;

/**
 * {@link ConfigDeserializer} implementation that uses a {@link Predicate} to determine which {@link ConfigElement}s can be deserialized, as
 * opposed to using an explicit type such as {@link TypeConfigDeserializer}.
 */
public abstract class PredicateConfigDeserializer<T> implements ConfigDeserializer<T> {

    private final Predicate<ConfigElement<? extends T>> predicate;

    public PredicateConfigDeserializer(Predicate<ConfigElement<? extends T>> predicate) {
        this.predicate = predicate;
    }

    @Override
    public final boolean canDeserialize(ConfigElement<T> element) {
        return this.predicate.test(element);
    }

}