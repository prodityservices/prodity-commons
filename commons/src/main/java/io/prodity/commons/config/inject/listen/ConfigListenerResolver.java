package io.prodity.commons.config.inject.listen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public enum ConfigListenerResolver {

    ;

    public static List<ConfigListenerMethod> resolveFromClass(Class<?> clazz) throws IllegalArgumentException {
        Preconditions.checkNotNull(clazz, "clazz");

        final List<ConfigListenerMethod> methods = Lists.newArrayList();
        for (Method method : clazz.getDeclaredMethods()) {
            final Set<ListenerType> types = ListenerType.resolveTypes(method);
            if (!types.isEmpty()) {
                final ConfigListenerMethod listenerMethod = new ConfigListenerMethod(method, types);
                methods.add(listenerMethod);
            }
        }

        return methods;
    }

}