package io.prodity.commons.config.load.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.prodity.commons.config.annotate.inject.ConfigMethod;
import io.prodity.commons.config.load.element.ConfigElement;
import io.prodity.commons.config.load.element.ConfigElementResolver;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

public enum ConfigMethodResolver {

    ;

    @Nonnull
    public static List<ListenerConfigMethod> resolveListenerMethods(@Nonnull Class<?> clazz) throws IllegalArgumentException {
        Preconditions.checkNotNull(clazz, "clazz");

        final List<ListenerConfigMethod> methods = Lists.newArrayList();

        for (Method method : clazz.getDeclaredMethods()) {
            final Set<ListenerType> types = ListenerType.resolveTypes(method);
            if (types.isEmpty()) {
                continue;
            }

            final int parametersLength = method.getParameters().length;
            if (parametersLength > 0) {
                throw new IllegalArgumentException("method=" + method.toString() + " must have 0 parameters but has " + parametersLength);
            }
            final ListenerConfigMethod configMethod = new ListenerConfigMethod(method, types);
            methods.add(configMethod);
        }

        return methods;
    }

    @Nonnull
    public static List<InjectableConfigMethod> resolveInjectableMethods(@Nonnull Class<?> clazz) {
        Preconditions.checkNotNull(clazz, "clazz");

        final List<InjectableConfigMethod> methods = Lists.newArrayList();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(ConfigMethod.class)) {
                continue;
            }

            final List<ConfigElement> parameters = ConfigElementResolver.resolveParameterElements(method);
            final InjectableConfigMethod configMethod = new InjectableConfigMethod(method, parameters);
            methods.add(configMethod);
        }

        return methods;
    }

}