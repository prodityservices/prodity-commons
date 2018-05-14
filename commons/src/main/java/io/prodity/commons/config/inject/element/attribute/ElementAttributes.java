package io.prodity.commons.config.inject.element.attribute;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.deserialize.Colorize;
import io.prodity.commons.config.annotate.deserialize.Colorize.ColorizeState;
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

    private static final ElementAttributeSet DEFAULT_RESOLVERS;

    static {
        DEFAULT_RESOLVERS = new ElementAttributeSet();

        ElementAttributes.DEFAULT_RESOLVERS.add(ColorizeAttribute.RESOLVER);
        ElementAttributes.DEFAULT_RESOLVERS.add(RequiredAttribute.RESOLVER);
        ElementAttributes.DEFAULT_RESOLVERS.add(RepositoryAttribute.RESOLVER);
        ElementAttributes.DEFAULT_RESOLVERS.add(DeserializableTypeAttribute.RESOLVER);
        ElementAttributes.DEFAULT_RESOLVERS.add(DefaultValueAttribute.RESOLVER);
    }

    public static ElementAttributeSet getDefaultResolverSet() {
        return ElementAttributes.DEFAULT_RESOLVERS;
    }

    public static ElementAttributeSet createResolverSet() {
        return new ElementAttributeSet(ElementAttributes.DEFAULT_RESOLVERS);
    }

    /**
     * Executes {@link ElementAttributeSet#resolveValues(AnnotatedElement)} with the specified {@link AnnotatedElement} using the {@link
     * ElementAttributes#DEFAULT_RESOLVERS}.
     *
     * @param element the element to resolveValues from
     * @return the {@link Set} of resolved attributes.
     */
    public static Set<? extends ElementAttributeValue<?>> resolveAttributes(AnnotatedElement element) {
        Preconditions.checkNotNull(element, "element");
        return ElementAttributes.DEFAULT_RESOLVERS.resolveValues(element);
    }

    public static final class ColorizeAttribute extends ElementAttributeValue<ColorizeState> {

        public static final ElementAttributeResolver<ColorizeAttribute> RESOLVER =
            new ElementAttributeResolver<>(
                ColorizeAttribute.class,
                (element) -> element.isAnnotationPresent(Colorize.class),
                (element) -> new ColorizeAttribute(element.getAnnotation(Colorize.class).value())
            );

        public ColorizeAttribute(ColorizeState value) {
            super(Preconditions.checkNotNull(value, "value"));
        }

    }

    public static final class RequiredAttribute extends ElementAttributeValue<Boolean> {

        public static final ElementAttributeResolver<RequiredAttribute> RESOLVER =
            new ElementAttributeResolver<>(
                RequiredAttribute.class,
                (element) -> element.isAnnotationPresent(Required.class),
                (element) -> new RequiredAttribute(element.getAnnotation(Required.class).value())
            );

        public RequiredAttribute(boolean value) {
            super(value);
        }

    }

    public static final class RepositoryAttribute extends ElementAttributeValue<String> {

        public static final ElementAttributeResolver<RepositoryAttribute> RESOLVER =
            new ElementAttributeResolver<>(
                RepositoryAttribute.class,
                (element) -> element.isAnnotationPresent(LoadFromRepository.class),
                (element) -> new RepositoryAttribute(element.getAnnotation(LoadFromRepository.class).value())
            );

        public RepositoryAttribute(String value) {
            super(Preconditions.checkNotNull(value, "value"));
        }

    }

    public static final class DeserializableTypeAttribute extends ElementAttributeValue<Void> {

        public static final ElementAttributeResolver<DeserializableTypeAttribute> RESOLVER =
            new ElementAttributeResolver<>(
                DeserializableTypeAttribute.class,
                (element) -> {
                    final AnnotatedType elementType;
                    if (element instanceof Field) {
                        elementType = ((Field) element).getAnnotatedType();
                    } else if (element instanceof Parameter) {
                        elementType = ((Parameter) element).getAnnotatedType();
                    } else {
                        return false;
                    }
                    return elementType.isAnnotationPresent(ConfigSerializable.class);
                },
                (element) -> new DeserializableTypeAttribute()
            );


        public DeserializableTypeAttribute() {
            super(null);
        }

    }

    public static final class DefaultValueAttribute extends ElementAttributeValue<Class<? extends Supplier<?>>> {

        public static final ElementAttributeResolver<DefaultValueAttribute> RESOLVER =
            new ElementAttributeResolver<>(
                DefaultValueAttribute.class,
                (element) -> element.isAnnotationPresent(ConfigDefault.class),
                (element) -> new DefaultValueAttribute(element.getAnnotation(ConfigDefault.class).value())
            );

        public DefaultValueAttribute(Class<? extends Supplier<?>> value) {
            super(Preconditions.checkNotNull(value, "value"));
        }

    }

}