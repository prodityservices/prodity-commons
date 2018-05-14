package io.prodity.commons.config.inject.deserialize;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.message.color.Colorizer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import ninja.leaping.configurate.ConfigurationNode;

@Singleton
public class ConfigValueResolver {

    @Inject
    private Colorizer colorizer;

    @Inject
    public Optional<Object> resolveValue(ConfigElement element, ConfigurationNode node) {
        final Object rawValue = node.getValue();
        final Class<?> expectedType = element.getType();

        if (this.isOfMatchingType(expectedType, rawValue.getClass())) {
            return Optional.of(rawValue);
        }

        //TODO
        //
        // 1) look for type deserializer for the type of element (not from rawValue)
        //  - based on list element type if deserialize is list, or map deserialize type if deserialize is map (key would be the key for each deserialize)
        //      if found, get and apply to the current node and return the deserialize.
        //
        // 2) create instance of the element type & a ConfigObject, then inject it.

        return Optional.empty();
    }

    public Object colorizeObject(Object object) {
        return this.modifyObject(object, String.class::isInstance, (obj) -> this.colorizer.translateColors((String) obj));
    }

    public Object modifyObject(Object object, Predicate<Object> modifyPredicate, Function<Object, Object> modifier) {
        if (modifyPredicate.test(object)) {
            return modifier.apply(object);
        }
        if (object instanceof List) {
            final List<?> list = (List<?>) object;
            final List<Object> modifiedList = Lists.newArrayList();
            for (Object element : list) {
                final Object modifiedObject = this.modifyObject(element, modifyPredicate, modifier);
                modifiedList.add(modifiedObject);
            }
            return modifiedList;
        }
        if (object instanceof Map) {
            final Map<?, ?> map = (Map<?, ?>) object;
            final Map<Object, Object> modifiedMap = Maps.newHashMap();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                final Object value = entry.getValue();
                final Object modifiedValue = this.modifyObject(value, modifyPredicate, modifier);
                modifiedMap.put(entry.getKey(), modifiedValue);
            }
            return modifiedMap;
        }
        return object;
    }

}