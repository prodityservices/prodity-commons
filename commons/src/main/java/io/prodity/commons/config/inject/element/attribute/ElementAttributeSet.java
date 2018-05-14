package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ElementAttributeSet {

    private final Set<ElementAttribute<?>> attributes;

    public ElementAttributeSet() {
        this.attributes = Sets.newConcurrentHashSet();
    }

    public ElementAttributeSet(ElementAttributeSet attributeSet) {
        Preconditions.checkNotNull(attributeSet, "attributeSet");
        this.attributes = Sets.newConcurrentHashSet(attributeSet.attributes);
    }

    public ElementAttributeSet add(ElementAttribute<?> resolver) {
        Preconditions.checkNotNull(resolver, "resolver");
        this.attributes.add(resolver);
        return this;
    }

    /**
     * @return a newly created {@link Set} of the present {@link ElementAttribute}s
     */
    public Set<ElementAttribute<?>> getAttributes() {
        return Sets.newHashSet(this.attributes);
    }

    /**
     * Resolves the {@link ElementAttributeValue}s from the specified {@link AnnotatedType}.
     *
     * @param element the element to resolveValues attributes from
     * @return the {@link Set} of resolved attributes, possibly empty but not null.
     */
    public Set<? extends ElementAttributeValue<?>> resolveValues(AnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");

        return this.attributes.stream()
            .map((attribute) -> attribute.getValue(element))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

}