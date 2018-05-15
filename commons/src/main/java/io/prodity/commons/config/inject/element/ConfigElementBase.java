package io.prodity.commons.config.inject.element;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeKey;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.reflect.element.DelegateNamedAnnotatedElement;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigElementBase<T> implements ConfigElement<T>, DelegateNamedAnnotatedElement {

    private final TypeToken<T> type;
    private final NamedAnnotatedElement element;

    private final String path;
    private final Map<ElementAttributeKey<?>, ElementAttributeValue<?>> attributes;

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
        ElementAttributes.resolveAttributes(this).forEach((value) -> this.attributes.put(value.getAttributeKey(), value));
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
    public boolean hasAttribute(@Nullable ElementAttributeKey<?> key) {
        return key != null && this.attributes.containsKey(key);
    }

    protected <V> Optional<ElementAttributeValue<V>> getRawAttribute(@Nullable ElementAttributeKey<V> key) {
        if (key == null || !this.attributes.containsKey(key)) {
            return Optional.empty();
        }
        final ElementAttributeValue<V> attribute = (ElementAttributeValue<V>) this.attributes.get(key);
        return Optional.of(attribute);
    }

    @Override
    public <V> Optional<V> getAttribute(@Nullable ElementAttributeKey<V> key) {
        return this.getRawAttribute(key).flatMap(ElementAttributeValue::getValue);
    }

    @Override
    public T resolve(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        final ConfigurationNode valueNode = node.getNode(this.getPath());
        return elementResolver.resolveValue(this, valueNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
            .add("path", this.path)
            .add("type", this.type)
            .add("attributes", this.attributes)
            .toString();
    }

}