package io.prodity.commons.config.inject.element;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.reflect.element.DelegateNamedAnnotatedElement;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.util.Map;
import java.util.Optional;

public class ConfigElementBase<T> implements ConfigElement<T>, DelegateNamedAnnotatedElement {

    private final TypeToken<T> type;
    private final NamedAnnotatedElement element;

    private final String path;
    private final Map<Class<? extends ElementAttributeValue>, ElementAttributeValue<?>> attributes;

    protected ConfigElementBase(TypeToken<T> type, NamedAnnotatedElement element) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(element, "element");
        this.element = element;
        this.path = ConfigElement.resolvePath(element);

        this.type = type;
        this.attributes = Maps.newIdentityHashMap();
        this.resolveAttributes();
    }

    private void resolveAttributes() {
        ElementAttributes.resolveAttributes(this).forEach((attribute) -> this.attributes.put(attribute.getClass(), attribute));
    }

    @Override
    public TypeToken<T> getType() {
        return this.type;
    }

    @Override
    public NamedAnnotatedElement getAnnotatedElement() {
        return this.element;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean hasAttribute(Class<? extends ElementAttributeValue> attributeClass) {
        return this.attributes.containsKey(attributeClass);
    }

    protected <V> Optional<ElementAttributeValue<V>> getAttribute(Class<? extends ElementAttributeValue<V>> attributeClass) {
        if (!this.attributes.containsKey(attributeClass)) {
            return Optional.empty();
        }
        final ElementAttributeValue<V> attribute = (ElementAttributeValue<V>) this.attributes.get(attributeClass);
        return Optional.of(attribute);
    }

    @Override
    public <V> Optional<V> getAttributeValue(Class<? extends ElementAttributeValue<V>> attributeClass) {
        return this.getAttribute(attributeClass).flatMap(ElementAttributeValue::getValue);
    }

}