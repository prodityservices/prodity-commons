package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.deserialize.Colorize;
import io.prodity.commons.config.annotate.deserialize.ConfigDefault;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.annotate.inject.Required;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.function.Supplier;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

public enum ElementAttributes {

    ;

    private static final ElementAttributeSet DEFAULT_ATTRIBUTES;

    public static final ElementAttributeKey<Boolean> COLORIZE_KEY = ElementAttributeKey.createKey("COLORIZE");
    public static final ElementAttributeKey<String> REPOSITORY_KEY = ElementAttributeKey.createKey("REPOSITORY");
    public static final ElementAttributeKey<Boolean> REQUIRED_KEY = ElementAttributeKey.createKey("REQUIRED");
    public static final ElementAttributeKey<Boolean> DESERIALIZABLE_KEY = ElementAttributeKey.createKey("DESERIALIZABLE");
    public static final ElementAttributeKey<Class<? extends Supplier<?>>> DEFAULT_VALUE_KEY = ElementAttributeKey
        .createKey("DEFAULT_VALUE");

    public static final ElementAttribute<Boolean> COLORIZE_ATTRIBUTE = ElementAttribute.<Boolean>builder()
        .setKey(ElementAttributes.COLORIZE_KEY)
        .setPredicate((element) -> element.isAnnotationPresent(Colorize.class))
        .setValueFunction((element) -> element.getAnnotation(Colorize.class).value())
        .build();

    public static final ElementAttribute<String> REPOSITORY_ATTRIBUTE = ElementAttribute.<String>builder()
        .setKey(ElementAttributes.REPOSITORY_KEY)
        .addConflicting(ElementAttributes.DESERIALIZABLE_KEY)
        .setPredicate((element) -> element.isAnnotationPresent(LoadFromRepository.class))
        .setValueFunction((element) -> element.getAnnotation(LoadFromRepository.class).value())
        .build();

    public static final ElementAttribute<Boolean> REQUIRED_ATTRIBUTE = ElementAttribute.<Boolean>builder()
        .setKey(ElementAttributes.REQUIRED_KEY)
        .setPredicate((element) -> element.isAnnotationPresent(Required.class))
        .setValueFunction((element) -> element.getAnnotation(Required.class).value())
        .build();

    public static final ElementAttribute<Boolean> DESERIALIZABLE_ATTRIBUTE = ElementAttribute.<Boolean>builder()
        .setKey(ElementAttributes.DESERIALIZABLE_KEY)
        .addConflicting(ElementAttributes.REPOSITORY_KEY)
        .setPredicate((element) -> {
            final AnnotatedType elementType;
            if (element instanceof Field) {
                elementType = ((Field) element).getAnnotatedType();
            } else if (element instanceof Parameter) {
                elementType = ((Parameter) element).getAnnotatedType();
            } else {
                return false;
            }
            return elementType.isAnnotationPresent(ConfigSerializable.class);
        })
        .setValueFunction((element) -> true)
        .build();

    public static final ElementAttribute<Class<? extends Supplier<?>>> DEFAULT_VALUE_ATTRIBUTE = ElementAttribute.<Class<? extends Supplier<?>>>builder()
        .setKey(ElementAttributes.DEFAULT_VALUE_KEY)
        .setPredicate((element) -> element.isAnnotationPresent(ConfigDefault.class))
        .setValueFunction((element) -> element.getAnnotation(ConfigDefault.class).value())
        .build();

    static {
        DEFAULT_ATTRIBUTES = new ElementAttributeSet();

        ElementAttributes.DEFAULT_ATTRIBUTES.add(ElementAttributes.COLORIZE_ATTRIBUTE);
        ElementAttributes.DEFAULT_ATTRIBUTES.add(ElementAttributes.REPOSITORY_ATTRIBUTE);
        ElementAttributes.DEFAULT_ATTRIBUTES.add(ElementAttributes.REQUIRED_ATTRIBUTE);
        ElementAttributes.DEFAULT_ATTRIBUTES.add(ElementAttributes.DESERIALIZABLE_ATTRIBUTE);
        ElementAttributes.DEFAULT_ATTRIBUTES.add(ElementAttributes.DEFAULT_VALUE_ATTRIBUTE);
    }

    public static ElementAttributeSet getDefaultAttributeSet() {
        return ElementAttributes.DEFAULT_ATTRIBUTES;
    }

    public static ElementAttributeSet createAttributeSet() {
        return new ElementAttributeSet(ElementAttributes.DEFAULT_ATTRIBUTES);
    }

    /**
     * Executes {@link ElementAttributeSet#resolveValues(AnnotatedElement)} with the specified {@link AnnotatedElement} using the {@link
     * ElementAttributes#DEFAULT_ATTRIBUTES}.
     *
     * @param element the element to resolveValues from
     * @return the {@link Set} of resolved attributes.
     */
    public static Set<? extends ElementAttributeValue<?>> resolveAttributes(AnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");
        return ElementAttributes.DEFAULT_ATTRIBUTES.resolveValues(element);
    }

}