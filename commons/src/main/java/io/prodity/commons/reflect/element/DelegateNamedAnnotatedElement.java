package io.prodity.commons.reflect.element;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface DelegateNamedAnnotatedElement extends NamedAnnotatedElement {

    NamedAnnotatedElement getAnnotatedElement();

    @Override
    default AnnotatedElement getHandle() {
        return this.getAnnotatedElement().getHandle();
    }

    @Override
    default <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return this.getAnnotatedElement().getAnnotation(annotationClass);
    }

    @Override
    default Annotation[] getAnnotations() {
        return this.getAnnotatedElement().getAnnotations();
    }

    @Override
    default Annotation[] getDeclaredAnnotations() {
        return this.getAnnotatedElement().getDeclaredAnnotations();
    }
    
}