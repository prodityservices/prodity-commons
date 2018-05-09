package io.prodity.commons.config.load.element;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.prodity.commons.reflect.NamedAnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import javax.annotation.Nonnull;

public enum ConfigElementResolver {

    ;

    @Nonnull
    public static List<ConfigFieldElement> resolveFieldElements(@Nonnull Class<?> clazz) {
        Preconditions.checkNotNull(clazz, "clazz");

        final List<ConfigFieldElement> elements = Lists.newArrayList();
        for (Field field : clazz.getDeclaredFields()) {
            if (!ConfigElement.isElement(field)) {
                continue;
            }

            final ConfigFieldElement element = new ConfigFieldElement(field);
            elements.add(element);
        }

        return elements;
    }

    @Nonnull
    public static List<ConfigElement> resolveParameterElements(@Nonnull Method method) throws IllegalArgumentException {
        Preconditions.checkNotNull(method, "method");

        final List<ConfigElement> elements = Lists.newArrayList();
        for (Parameter parameter : method.getParameters()) {
            if (ConfigElement.isElement(parameter)) {
                throw new IllegalArgumentException(
                    "method=" + method.toString() + " parameter=" + parameter.getName() + " does not have the @ConfigPath annotation");
            }

            final NamedAnnotatedElement annotatedElement = NamedAnnotatedElement.fromParameter(parameter);
            final ConfigElement element = new ConfigElement(annotatedElement);
            elements.add(element);
        }

        return elements;
    }

}