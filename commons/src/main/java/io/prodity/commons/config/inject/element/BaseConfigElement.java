package io.prodity.commons.config.inject.element;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeKey;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.reflect.element.DelegateNamedAnnotatedElement;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;

public class BaseConfigElement<T> implements ConfigElement<T>, DelegateNamedAnnotatedElement {

    private final TypeToken<T> type;
    private final NamedAnnotatedElement element;

    private final ElementInjectionStrategy injectionStrategy;
    private final List<String> path;
    private final Map<ElementAttributeKey<?>, ElementAttributeValue<?>> attributes;

    /**
     * Automatically resolves attributes & paths from the specified values.<br>
     *
     * @param type the {@link TypeToken} of the type
     * @param element the {@link NamedAnnotatedElement}
     */
    public BaseConfigElement(TypeToken<T> type, NamedAnnotatedElement element) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(element, "element");
        this.element = element;
        this.injectionStrategy = ConfigElement.resolveInjectionStrategy(element);
        if (this.injectionStrategy == ElementInjectionStrategy.NODE_PATH) {
            this.path = ConfigElement.resolvePath(element);
        } else {
            this.path = null;
        }
        this.type = type;
        this.attributes = Maps.newConcurrentMap();
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
    public ElementInjectionStrategy getInjectionStrategy() {
        return this.injectionStrategy;
    }

    @Override
    public List<String> getPath() throws IllegalStateException {
        if (this.path == null) {
            throw new IllegalStateException("element=" + this.toString() + " does not have a path");
        }
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
    public List<ElementAttributeValue<?>> getAttributes() {
        return ImmutableList.copyOf(this.attributes.values());
    }

    @Override
    public T resolve(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        final ConfigurationNode valueNode;

        if (this.injectionStrategy == ElementInjectionStrategy.NODE_KEY) {
            valueNode = SimpleConfigurationNode.root().setValue(node.getKey());
        } else {
            valueNode = node.getNode(this.path.toArray());
        }

        return elementResolver.resolveValue(this, valueNode);
    }

    @Override
    public String toString() {
        final ToStringHelper toStringHelper = MoreObjects.toStringHelper(this.getClass())
            .add("type", this.type)
            .add("injectionStrategy", this.injectionStrategy)
            .add("attributes", this.attributes);
        if (this.path != null) {
            toStringHelper.add("path", this.path);
        }
        return toStringHelper.toString();
    }

}