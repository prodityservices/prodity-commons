package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ElementAttribute<V> {

    public static final class Builder<V> {

        private final ImmutableMap.Builder<ElementAttributeKey<?>, Predicate<? extends ElementAttributeValue<?>>> conflicting;
        private ElementAttributeKey<V> key;
        private Predicate<AnnotatedElement> predicate;
        private Function<AnnotatedElement, V> valueFunction;

        private Builder() {
            this.conflicting = ImmutableMap.builder();
        }

        public ElementAttribute.Builder<V> setKey(ElementAttributeKey<V> key) {
            Preconditions.checkNotNull(key, "key");
            this.key = key;
            return this;
        }

        public ElementAttribute.Builder<V> addConflicting(ElementAttributeKey<?> conflictingKey) {
            return this.addConflicting(conflictingKey, (value) -> true);
        }

        public <V2> ElementAttribute.Builder<V> addConflicting(ElementAttributeKey<V2> conflictingKey,
            Predicate<ElementAttributeValue<V2>> valuePredicate) {
            Preconditions.checkNotNull(conflictingKey, "conflictingKey");
            Preconditions.checkNotNull(valuePredicate, "valuePredicate");

            this.conflicting.put(conflictingKey, valuePredicate);
            return this;
        }

        public ElementAttribute.Builder<V> setPredicate(Predicate<AnnotatedElement> predicate) {
            Preconditions.checkNotNull(predicate, "predicate");
            this.predicate = predicate;
            return this;
        }

        public ElementAttribute.Builder<V> setValueFunction(Function<AnnotatedElement, V> valueFunction) {
            Preconditions.checkNotNull(valueFunction, "valueFunction");
            this.valueFunction = valueFunction;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.conflicting, "conflicting");
            Preconditions.checkNotNull(this.predicate, "predicate");
            Preconditions.checkNotNull(this.valueFunction, "valueFunction");
        }

        public ElementAttribute<V> build() {
            return new ElementAttribute<>(this.key, this.predicate, this.valueFunction, this.conflicting.build());
        }

    }

    public static <V> ElementAttribute.Builder<V> builder() {
        return new ElementAttribute.Builder<>();
    }

    private final ElementAttributeKey<V> key;

    private final Map<ElementAttributeKey<?>, Predicate<? extends ElementAttributeValue<?>>> conflicting;
    private final Predicate<AnnotatedElement> predicate;
    private final Function<AnnotatedElement, V> valueFunction;

    private ElementAttribute(ElementAttributeKey<V> key, Predicate<AnnotatedElement> predicate,
        Function<AnnotatedElement, V> valueFunction,
        ImmutableMap<ElementAttributeKey<?>, Predicate<? extends ElementAttributeValue<?>>> conflicting) {

        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(predicate, "predicate");
        Preconditions.checkNotNull(valueFunction, "valueFunction");
        Preconditions.checkNotNull(conflicting, "conflicting");

        this.key = key;
        this.predicate = predicate;
        this.valueFunction = valueFunction;
        this.conflicting = conflicting;
    }

    public ElementAttributeKey<V> getKey() {
        return this.key;
    }

    public <V2> boolean isConflicting(ElementAttributeValue<V2> value) {
        Preconditions.checkNotNull(value, "value");

        if (!this.conflicting.containsKey(value.getAttributeKey())) {
            return false;
        }

        final Predicate<ElementAttributeValue<V2>> valuePredicate = (Predicate<ElementAttributeValue<V2>>) this.conflicting
            .get(value.getAttributeKey());
        return valuePredicate.test(value);
    }

    public boolean isPresent(AnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");
        return this.predicate.test(element);
    }

    /**
     * Resolves the {@link ElementAttributeValue} from the specified {@link AnnotatedElement} if the element has this attribute
     *
     * @param element the element
     * @return the {@link ElementAttributeValue} of this {@link ElementAttribute} on the specified element
     * @throws IllegalStateException if this attribute is not present on the specified element
     */
    public ElementAttributeValue<V> getValue(AnnotatedElement element) throws IllegalStateException {
        Preconditions.checkNotNull(element, "element");
        if (!this.isPresent(element)) {
            throw new IllegalStateException("attribute=" + this.toString() + " is not present on element=" + element);
        }
        final V value = this.valueFunction.apply(element);
        return new ElementAttributeValue<>(this, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("key", this.key.getName())
            .toString();
    }

}