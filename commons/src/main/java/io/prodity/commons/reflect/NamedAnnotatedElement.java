package io.prodity.commons.reflect;

import com.google.common.base.Preconditions;
import io.prodity.commons.name.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NamedAnnotatedElement implements AnnotatedElement, Named {

    @Nonnull
    public static <T extends AnnotatedElement> NamedAnnotatedElement from(@Nonnull T element, @Nonnull Function<T, String> nameResolver) {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(nameResolver, "nameResolver");

        final String name = nameResolver.apply(element);
        Preconditions.checkNotNull(name, "name");

        return new NamedAnnotatedElement(element, name);
    }

    @Nonnull
    public static NamedAnnotatedElement fromParameter(@Nonnull Parameter parameter) {
        return NamedAnnotatedElement.from(parameter, Parameter::getName);
    }

    @Nonnull
    public static NamedAnnotatedElement fromField(@Nonnull Field field) {
        return NamedAnnotatedElement.from(field, Field::getName);
    }

    private final AnnotatedElement element;
    private final String name;

    protected NamedAnnotatedElement(@Nonnull AnnotatedElement element, @Nonnull String name) {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(name, "name");

        this.element = element;
        this.name = name;
    }

    @Nonnull
    public AnnotatedElement getHandle() {
        return this.element;
    }

    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override
    @Nullable
    public <T extends Annotation> T getAnnotation(@Nonnull Class<T> annotationClass) {
        return this.element.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.element.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return this.element.getDeclaredAnnotations();
    }

}