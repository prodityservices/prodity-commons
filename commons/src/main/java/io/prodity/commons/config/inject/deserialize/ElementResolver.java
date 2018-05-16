package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.message.color.Colorizer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementResolver {

    @Inject
    private ElementDeserializerRegistry deserializerRegistry;

    @Inject
    private Colorizer colorizer;

    @Inject
    private ServiceLocator serviceLocator;

    @Nullable
    public <T> T resolveValue(ConfigElement<T> element, ConfigurationNode node) throws Throwable {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(node, "node");

        final ElementDeserializer<? extends T> deserializer = this.deserializerRegistry.get(element.getType());

        if (node.isVirtual()) {
            if (element.getAttributeOrDefault(ElementAttributes.REQUIRED_KEY, false)) {
                throw new IllegalStateException(
                    "element=" + element.toString() + " is required but node=" + node.toString() + " is virtual (not present)");
            }

            return null;
        }

        T object = deserializer.deserialize(element.getType(), node);

        if (object == null) {
            if (element.getAttribute(ElementAttributes.REQUIRED_KEY).orElse(false)) {
                throw new IllegalStateException("element=" + element + " is required but deserialized value=null");
            }

            return null;
        }

        if (element.getAttributeOrDefault(ElementAttributes.COLORIZE_KEY, false)) {
            object = this.colorizeObject(object);
        }

        //TODO default value attribute (invoke Supplier)
        //TODO repository attribute (serviceLocator)

        return object;
    }

    private <T> T colorizeObject(T object) {
        return (T) this.modifyObject(object, String.class::isInstance, (obj) -> this.colorizer.translateColors((String) obj));
    }

    private Object modifyObject(Object object, Predicate<Object> modifyPredicate, Function<Object, Object> modifier) {
        if (modifyPredicate.test(object)) {
            return modifier.apply(object);
        }
        if (object instanceof List) {
            final List<Object> list = (List<Object>) object;
            for (int i = 0; i < list.size(); i++) {
                final Object element = list.get(i);
                final Object modifiedElement = this.modifyObject(element, modifyPredicate, modifier);
                list.set(i, modifiedElement);
            }
            return list;
        }
        if (object instanceof Map) {
            final Map<?, Object> map = (Map<?, Object>) object;
            for (Map.Entry<?, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                final Object modifiedValue = this.modifyObject(value, modifyPredicate, modifier);
                entry.setValue(modifiedValue);
            }
            return map;
        }
        return object;
    }

}