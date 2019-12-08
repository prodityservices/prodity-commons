package io.prodity.commons.reflect.element;

import com.google.common.base.Preconditions;
import io.prodity.commons.name.Named;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.function.Function;

public interface NamedAnnotatedElement extends AnnotatedElement, Named {

    static <T extends AnnotatedElement> NamedAnnotatedElement from(T element, Function<T, String> nameResolver) {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(nameResolver, "nameResolver");

        final String name = nameResolver.apply(element);
        Preconditions.checkNotNull(name, "name");

        return new SimpleNamedAnnotatedElement(element, name);
    }

    static NamedAnnotatedElement fromParameter(Parameter parameter) {
        return NamedAnnotatedElement.from(parameter, Parameter::getName);
    }

    static NamedAnnotatedElement fromField(Field field) {
        return NamedAnnotatedElement.from(field, Field::getName);
    }

    AnnotatedElement getHandle();

}