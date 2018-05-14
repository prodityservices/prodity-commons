package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.inject.ConfigIgnore;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

public interface ConfigElement<T> extends NamedAnnotatedElement {

    static String resolvePath(NamedAnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");

        return Optional.ofNullable(element.getAnnotation(ConfigPath.class))
            .map(ConfigPath::value)
            .orElseGet(element::getName);
    }

    static boolean isElement(AnnotatedElement element) {
        return !element.isAnnotationPresent(ConfigIgnore.class);
    }

    TypeToken<T> getType();

    String getPath();

    boolean hasAttribute(Class<? extends ElementAttributeValue> attributeClass);

    <V> Optional<V> getAttributeValue(Class<? extends ElementAttributeValue<V>> attributeClass);

}