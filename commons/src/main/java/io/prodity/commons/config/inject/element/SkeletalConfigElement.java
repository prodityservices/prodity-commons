package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * Extremely simple {@link ConfigElement} implementation with no attributes.
 */
public class SkeletalConfigElement<T> implements ConfigElement<T> {

    private final TypeToken<T> type;

    public SkeletalConfigElement(TypeToken<T> type) {
        Preconditions.checkNotNull(type, "type");
        this.type = type;
    }

    @Override
    public TypeToken<T> getType() {
        return this.type;
    }

    @Override
    @Null
    public String getPath() {
        return null;
    }

    @Override
    public boolean hasAttribute(@Nullable ElementAttributeKey<?> key) {
        return false;
    }

    @Override
    public <V> Optional<V> getAttribute(@Nullable ElementAttributeKey<V> key) {
        return Optional.empty();
    }

    @Override
    @Null
    public AnnotatedElement getHandle() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }

    @Override
    public T resolve(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        return elementResolver.resolveValue(this, node);
    }

}