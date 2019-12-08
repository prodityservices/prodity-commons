package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.prodity.commons.name.Named;
import java.util.Objects;

/**
 * Simple class used as a key for {@link ElementAttribute}s.
 *
 * @param <V> the type of value associated with the {@link ElementAttribute}
 */
public class ElementAttributeKey<V> implements Named {

    /**
     * Creates a new {@link ElementAttributeKey} with the specified name.
     *
     * @param name the key name
     * @return the newly created key
     */
    public static <V> ElementAttributeKey<V> createKey(String name) {
        return new ElementAttributeKey<>(name);
    }

    private final String name;

    private ElementAttributeKey(String name) {
        Preconditions.checkNotNull(name);
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ElementAttributeKey)) {
            return false;
        }
        final ElementAttributeKey<?> that = (ElementAttributeKey<?>) object;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", this.name)
            .toString();
    }

}