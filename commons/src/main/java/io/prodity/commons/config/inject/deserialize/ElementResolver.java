package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import ninja.leaping.configurate.ConfigurationNode;
import org.glassfish.hk2.api.ServiceLocator;

@Singleton
public class ElementResolver {

    @Inject
    private ElementDeserializerRegistry deserializerRegistry;

    @Inject
    private ElementColorizer elementColorizer;

    @Inject
    private ElementRepositoryResolver repositoryLoader;

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private ConfigInjectionContext injectionContext;

    public ServiceLocator getServiceLocator() {
        return this.serviceLocator;
    }

    public ElementDeserializerRegistry getDeserializerRegistry() {
        return this.deserializerRegistry;
    }

    @Nullable
    public <T> T resolveValue(ConfigElement<T> element, ConfigurationNode node) throws Throwable {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(node, "node");

        final ElementDeserializer<? extends T> deserializer = this.deserializerRegistry.get(element.getType());

        Object value = null;

        if (node.isVirtual()) {
            value = this.handleNullCheck(element, value, () -> {
                final List<String> nodePath = Arrays.stream(node.getPath()).map(Object::toString).collect(Collectors.toList());
                final String nodePathString = String.join(".", nodePath);
                throw new IllegalStateException(
                    "element=" + element.toString() + " is required but node=" + nodePathString + " is virtual (not present)");
            });
        } else if (element.hasAttribute(ElementAttributes.REPOSITORY_KEY)) {
            value = this.repositoryLoader.resolveFromRepository(element, node);
        } else {
            value = deserializer.deserialize(this.injectionContext, element.getType(), node);
            value = this.handleNullCheck(element, value, () -> "element=" + element + " is required but deserialized value=null");
        }

        if (value == null) {
            return null;
        }

        if (element.hasAttribute(ElementAttributes.COLORIZE_KEY)) {
            value = this.elementColorizer.colorize(value);
        }

        if (value != null && !element.getType().wrap().getRawType().isInstance(value)) {
            throw new IllegalStateException(
                "element=" + element.toString() + " valueType=" + value.getClass().getName() + " is not an instance of the  required type="
                    + element.getType().toString());
        }

        return value == null ? null : (T) value;
    }

    @Nullable
    Object handleNullCheck(ConfigElement<?> element, @Nullable Object currentValue, Supplier<String> requiredErrorMessage) {
        if (currentValue != null) {
            return currentValue;
        }

        if (element.hasAttribute(ElementAttributes.REQUIRED_KEY)) {
            throw new IllegalStateException(requiredErrorMessage.get());
        }

        if (element.hasAttribute(ElementAttributes.DEFAULT_VALUE_KEY)) {
            return this.resolveFromDefault(element);
        }

        return null;
    }

    private Object resolveFromDefault(ConfigElement<?> element) {
        if (!element.hasAttribute(ElementAttributes.DEFAULT_VALUE_KEY)) {
            throw new IllegalStateException(
                "can't resolve default value from element=" + element.toString() + " as it does not have a default value");
        }

        final Class<? extends Supplier<?>> defaultSupplier = element.getAttribute(ElementAttributes.DEFAULT_VALUE_KEY).get();
        final Supplier<?> supplierInstance;
        try {
            supplierInstance = defaultSupplier.newInstance();
        } catch (Throwable throwable) {
            throw new IllegalStateException(
                "defaultSupplier=" + defaultSupplier.getName() + " for element=" + element.toString() + " could not be instantiated",
                throwable);
        }

        final Object object = supplierInstance.get();
        if (object == null) {
            throw new IllegalStateException(
                "element=" + element.toString() + " has a default supplier=" + defaultSupplier.getName() + " that returns a null value");
        }

        if (!element.getType().getRawType().isInstance(object)) {
            throw new IllegalStateException(
                "defaultValue=" + object + " of element=" + element.toString() + " is not of the element's type=" + element.getType()
                    .toString());
        }

        return object;
    }

}