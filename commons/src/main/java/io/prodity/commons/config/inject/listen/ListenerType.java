package io.prodity.commons.config.inject.listen;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.listen.PostConfigInject;
import io.prodity.commons.config.annotate.listen.PreConfigInject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ListenerType {

    PRE_INJECT(PreConfigInject.class),
    POST_INJECT(PostConfigInject.class);

    public static Set<ListenerType> resolveTypes(Method method) {
        Preconditions.checkNotNull(method, "method");

        return Arrays.stream(ListenerType.values())
            .filter((attribute) -> method.isAnnotationPresent(attribute.getAnnotationClass()))
            .collect(Collectors.toSet());
    }

    private final Class<? extends Annotation> annotationClass;

    ListenerType(Class<? extends Annotation> annotationClass) {
        Preconditions.checkNotNull(annotationClass);
        this.annotationClass = annotationClass;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }

}