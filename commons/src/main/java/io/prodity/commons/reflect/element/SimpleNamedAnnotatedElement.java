package io.prodity.commons.reflect.element;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.Nullable;

public class SimpleNamedAnnotatedElement implements NamedAnnotatedElement {

    private final AnnotatedElement element;
    private final String name;

    protected SimpleNamedAnnotatedElement(AnnotatedElement element, String name) {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(name, "name");

        this.element = element;
        this.name = name;
    }

    @Override
    public AnnotatedElement getHandle() {
        return this.element;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @Nullable
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
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