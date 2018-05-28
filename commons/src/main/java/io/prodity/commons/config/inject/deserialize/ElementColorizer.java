package io.prodity.commons.config.inject.deserialize;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.prodity.commons.effect.message.color.Colorizer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementColorizer {

    private final Map<Predicate<? extends Class<?>>, Function<Object, Object>> supportedTypes = Maps.newConcurrentMap();

    @Inject
    private Colorizer colorizer;

    @PostConstruct
    private void addDefaults() {
        this.addType(String.class, this::colorizeString);
        this.addType(Map.class, this::colorizeMap);
        this.addType(List.class, this::colorizeList);
        this.addType(Set.class, this::colorizeSet);
        this.addType(Class::isArray, this::colorizeArray);
    }

    public <T> ElementColorizer addType(Class<? extends T> typeClass, Function<T, T> colorizerFunction) {
        return this.addType((clazz) -> typeClass.isAssignableFrom(clazz), colorizerFunction);
    }

    public <T> ElementColorizer addType(Predicate<Class<? extends T>> classPredicate, Function<T, T> colorizerFunction) {
        this.supportedTypes.put(classPredicate, (Function<Object, Object>) colorizerFunction);
        return this;
    }

    private Optional<Function<Object, Object>> getColorizerFunction(Class<?> clazz) {
        for (Entry<Predicate<? extends Class<?>>, Function<Object, Object>> entry : this.supportedTypes.entrySet()) {
            final Predicate<Class<?>> predicate = (Predicate<Class<?>>) entry.getKey();
            if (predicate.test(clazz)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Nullable
    public Object colorize(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        return this.colorizeObject(object);
    }

    private void verifyType(Class<?> expectedType, Object object) {
        if (!expectedType.isInstance(object)) {
            throw new IllegalArgumentException(
                "object=" + object.getClass().getName() + " must be an instance of " + expectedType.getName());
        }
    }

    @Nullable
    private Object colorizeObject(@Nullable Object object) {
        return object == null ? null
            : this.getColorizerFunction(object.getClass()).map((function) -> function.apply(object)).orElse(object);
    }

    private Object colorizeString(Object object) {
        this.verifyType(String.class, object);
        final String string = (String) object;
        return this.colorizer.translateColors(string);
    }

    private Object colorizeList(Object object) {
        this.verifyType(List.class, object);

        final List list = (List) object;
        final boolean immutable = list instanceof ImmutableList;

        final List newList = immutable ? Lists.newArrayList() : list;

        for (int index = 0; index < list.size(); index++) {
            final Object element = list.get(index);
            final Object colorizedElement = this.colorizeObject(element);

            if (immutable) {
                newList.add(colorizedElement);
            } else {
                newList.set(index, colorizedElement);
            }
        }

        return immutable ? ImmutableList.copyOf(newList) : newList;
    }

    private Object colorizeSet(Object object) {
        this.verifyType(Set.class, object);

        final Set set = (Set) object;
        final Set newSet = Sets.newHashSet();

        for (Object element : set) {
            final Object colorizedElement = this.colorizeObject(element);
            newSet.add(colorizedElement);
        }

        if (set instanceof ImmutableSet) {
            return ImmutableSet.copyOf(newSet);
        } else {
            set.clear();
            set.addAll(newSet);
            return set;
        }
    }

    private Object colorizeMap(Object object) {
        this.verifyType(Map.class, object);

        final Map<?, ?> map = (Map) object;
        final boolean immutable = map instanceof ImmutableMap;

        final Map newMap = immutable ? Maps.newHashMap() : map;

        for (Map.Entry entry : map.entrySet()) {
            final Object value = entry.getValue();
            final Object colorizedValue = this.colorizeObject(value);
            if (immutable) {
                newMap.put(entry.getKey(), colorizedValue);
            } else {
                entry.setValue(colorizedValue);
            }
        }

        return immutable ? ImmutableMap.copyOf(newMap) : newMap;
    }

    private Object colorizeArray(Object object) {
        if (!object.getClass().isArray()) {
            throw new IllegalArgumentException(
                "object=" + object.getClass().getName() + " must be an array");
        }

        final Object[] array = (Object[]) object;

        for (int index = 0; index < array.length; index++) {
            final Object element = array[index];
            final Object colorizedElement = this.colorizeObject(element);
            array[index] = colorizedElement;
        }

        return array;
    }

}