package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ElementAttribute<V> {

    public static final class Builder<V> {

        private final ImmutableSet.Builder<Class<? extends ElementAttribute<?>>> conflicting;
        private Class<? extends ElementAttribute<V>> attributeClass;
        private Predicate<AnnotatedElement> predicate;
        private Function<AnnotatedElement, ElementAttributeValue<V>> valueFunction;

        private Builder() {
            this.conflicting = ImmutableSet.builder();
        }

        public ElementAttribute.Builder<V> addConflicting(Class<? extends ElementAttribute<?>> clazz) {
            Preconditions.checkNotNull(clazz, "clazz");
            Preconditions.checkNotNull(this.attributeClass != clazz,
                "specified Class=" + clazz.getName() + " is the same as attributeClass=" + this.attributeClass.getSimpleName());
            this.conflicting.add(clazz);
            return this;
        }

        public ElementAttribute.Builder<V> setClass(Class<? extends ElementAttribute<V>> attributeClass) {
            Preconditions.checkNotNull(attributeClass, "attributeClass");
            this.attributeClass = attributeClass;
            return this;
        }

        public ElementAttribute.Builder<V> setPredicate(Predicate<AnnotatedElement> predicate) {
            Preconditions.checkNotNull(predicate, "predicate");
            this.predicate = predicate;
            return this;
        }

        public ElementAttribute.Builder<V> setValueFunction(Function<AnnotatedElement, ElementAttributeValue<V>> valueFunction) {
            Preconditions.checkNotNull(valueFunction, "valueFunction");
            this.valueFunction = valueFunction;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.attributeClass, "attributeClass");
            Preconditions.checkNotNull(this.predicate, "predicate");
            Preconditions.checkNotNull(this.valueFunction, "valueFunction");
        }

        public ElementAttribute<V> build() {
            return new ElementAttribute<>(this.attributeClass, this.predicate, this.valueFunction, this.conflicting);
        }

    }

    public static <V> ElementAttribute.Builder<V> builder() {
        return new ElementAttribute.Builder<>();
    }

    private final Set<Class<? extends ElementAttribute<?>>> conflictingAttributes;

    private final Class<? extends ElementAttribute<V>> attributeClass;
    private final Predicate<AnnotatedElement> predicate;
    private final Function<AnnotatedElement, ElementAttributeValue<V>> valueFunction;

    protected ElementAttribute(Class<? extends ElementAttribute<V>> attributeClass, Predicate<AnnotatedElement> predicate,
        Function<AnnotatedElement, ElementAttributeValue<V>> valueFunction,
        ImmutableSet<Class<? extends ElementAttribute<?>> conflictingAttributes) {

        Preconditions.checkNotNull(attributeClass, "attributeClass");
        Preconditions.checkNotNull(predicate, "predicate");
        Preconditions.checkNotNull(valueFunction, "valueFunction");
        Preconditions.checkNotNull(conflictingAttributes, "conflictingAttributes");

        this.attributeClass = attributeClass;
        this.predicate = predicate;
        this.valueFunction = valueFunction;
        this.conflictingAttributes = conflictingAttributes;
    }

    /**
     * Gets an immutable {@link Set} of the classes of {@link ElementAttribute}s that conflict with this attribute.
     *
     * @return the immutable set
     */
    public Set<Class<? extends ElementAttribute<?>>> getConflictingAttributes() {
        return this.conflictingAttributes;
    }

    /**
     * Resolves the {@link ElementAttributeValue} from the specified {@link AnnotatedElement} if the element has this attribute
     *
     * @param element the element
     * @return a nonnull {@link Optional} containing a possible {@link ElementAttributeValue} of this attribute on the specified element
     */
    public Optional<ElementAttributeValue<V>> getValue(AnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");
        if (!this.predicate.test(element)) {
            return Optional.empty();
        }
        return Optional.of(this.valueFunction.apply(element));
    }

}