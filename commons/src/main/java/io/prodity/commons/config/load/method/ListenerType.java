package io.prodity.commons.config.load.method;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.listen.PostConfigLoad;
import io.prodity.commons.config.annotate.listen.PreConfigLoad;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public enum ListenerType {

    PRE_LOAD(PreConfigLoad.class),
    POST_LOAD(PostConfigLoad.class);

    @Nonnull
    public static Set<ListenerType> resolveTypes(@Nonnull Method method) {
        Preconditions.checkNotNull(method, "method");

        return Arrays.stream(ListenerType.values())
            .filter((attribute) -> method.isAnnotationPresent(attribute.getAnnotationClass()))
            .collect(Collectors.toSet());
    }

    private final Class<? extends Annotation> annotationClass;

    ListenerType(@Nonnull Class<? extends Annotation> annotationClass) {
        Preconditions.checkNotNull(annotationClass);
        this.annotationClass = annotationClass;
    }

    @Nonnull
    public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }

}