package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.ConfigNodeKey;
import io.prodity.commons.config.annotate.inject.ConfigNodeValue;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.ConfigObject;
import io.prodity.commons.config.inject.ConfigResolvable;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeKey;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * Represents an element of a {@link ConfigObject}
 *
 * @param <T> the type of the element
 */
public interface ConfigElement<T> extends ConfigResolvable<T>, NamedAnnotatedElement {

    ImmutableSet<Class<? extends Annotation>> ELEMENT_ANNOTATIONS = ImmutableSet.<Class<? extends Annotation>>builder()
        .add(ConfigInject.class)
        .add(ConfigPath.class)
        .add(ConfigNodeKey.class)
        .add(ConfigNodeValue.class)
        .build();

    String PERIOD_LITERAL = Pattern.quote(".");

    /**
     * Resolves the {@link ninja.leaping.configurate.ConfigurationNode} path of the specified {@link NamedAnnotatedElement}.
     *
     * @param element the element
     * @return an immutable {@link List} of the elements path
     */
    static List<String> resolvePath(NamedAnnotatedElement element) throws IllegalStateException {
        Preconditions.checkNotNull(element, "element");

        if (!ElementInjectionStrategy.NODE_PATH.hasStrategy(element)) {
            throw new IllegalStateException(
                "element=" + element.getName() + " does not have the ElementInjectionStrategy#NODE_PATH strategy");
        }

        final String pathAsString = Optional.ofNullable(element.getAnnotation(ConfigPath.class))
            .map(ConfigPath::value)
            .orElseGet(element::getName);

        return ImmutableList.copyOf(pathAsString.split(ConfigElement.PERIOD_LITERAL));
    }

    /**
     * Gets whether or not the specified {@link AnnotatedElement} is a {@link ConfigElement}.
     *
     * @param element the element
     * @return true if the specified element is a {@link ConfigElement}, false if not
     */
    static boolean isElement(@Nullable AnnotatedElement element) {
        if (element == null) {
            return false;
        }
        for (Class<? extends Annotation> annotation : ConfigElement.ELEMENT_ANNOTATIONS) {
            if (element.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    TypeToken<T> getType();

    ElementInjectionStrategy getInjectionStrategy();

    /**
     * Gets the element's path in a {@link ninja.leaping.configurate.ConfigurationNode}.
     *
     * @return an immutable {@link List} of the paths
     * @throws IllegalStateException if there is no path of this {@link ConfigElement}
     */
    List<String> getPath() throws IllegalStateException;

    boolean hasAttribute(@Nullable ElementAttributeKey<?> key);

    <V> Optional<V> getAttribute(@Nullable ElementAttributeKey<V> key);

    /**
     * Gets a {@link List} of all {@link ElementAttributeValue}s present in this element.
     *
     * @return an immutable {@link List} of the elements
     */
    List<ElementAttributeValue<?>> getAttributes();

    default <V> V getAttributeOrDefault(@Nullable ElementAttributeKey<V> key, @Nullable V defaultValue) {
        return this.getAttribute(key).orElse(defaultValue);
    }

}