package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.inject.ConfigIgnore;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.ConfigResolvable;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeKey;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Represents an element of a {@link io.prodity.commons.config.inject.object.ConfigObject}
 *
 * @param <T> the type of the element
 */
public interface ConfigElement<T> extends ConfigResolvable<T>, NamedAnnotatedElement {

    static String resolvePath(NamedAnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");

        return Optional.ofNullable(element.getAnnotation(ConfigPath.class))
            .map(ConfigPath::value)
            .orElseGet(element::getName);
    }

    /**
     * Gets whether or not the specified {@link AnnotatedElement} is a {@link ConfigElement}.
     *
     * @param element the element
     * @return true if the specified element is a {@link ConfigElement}, false if not.
     */
    static boolean isElement(@Nullable AnnotatedElement element) {
        return element != null && !element.isAnnotationPresent(ConfigIgnore.class);
    }

    /**
     * Creates a new {@link SkeletalConfigElement} for the specified {@link Class} type.
     *
     * @param type the type's {@link Class}
     * @param <T> the type
     * @return the created {@link SkeletalConfigElement}
     */
    static <T> ConfigElement<T> ofType(Class<T> type) {
        return ConfigElement.ofType(TypeToken.of(type));
    }

    /**
     * Creates a new {@link SkeletalConfigElement} for the specified {@link TypeToken}.
     *
     * @param type the {@link TypeToken}
     * @param <T> the type
     * @return the created {@link SkeletalConfigElement}
     */
    static <T> ConfigElement<T> ofType(TypeToken<T> type) {
        return new SkeletalConfigElement<>(type);
    }

    TypeToken<T> getType();

    String getPath();

    boolean hasAttribute(@Nullable ElementAttributeKey<?> key);

    <V> Optional<V> getAttribute(@Nullable ElementAttributeKey<V> key);

    default <V> V getAttributeOrDefault(@Nullable ElementAttributeKey<V> key, @Nullable V defaultValue) {
        return this.getAttribute(key).orElse(defaultValue);
    }

}