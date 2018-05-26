package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.util.Set;

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
     * @throws IllegalStateException if there are conflicting {@link ElementAttribute}s on the specified element
     */
    public Set<? extends ElementAttributeValue<?>> resolveValues(AnnotatedElement element) throws IllegalStateException {
        Preconditions.checkNotNull(element, "element");

        final Set<ElementAttributeValue<?>> values = Sets.newHashSet();

        for (ElementAttribute<?> attribute : this.attributes) {
            if (!attribute.isPresent(element)) {
                continue;
            }
            final ElementAttributeValue<?> value = attribute.getValue(element);

            for (ElementAttributeValue<?> possibleConflictingValue : values) {
                if (attribute.isConflicting(possibleConflictingValue)) {
                    throw new IllegalStateException(
                        "element=" + element + " has conflicting attributes '" + attribute.getKey() + "' & '" + possibleConflictingValue
                            .getAttributeKey() + "'");
                }
            }
            
            values.add(value);
        }

        return values;
    }

}