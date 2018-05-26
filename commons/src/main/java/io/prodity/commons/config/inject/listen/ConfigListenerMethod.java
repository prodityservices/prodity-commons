package io.prodity.commons.config.inject.listen;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.annotation.Nullable;

public class ConfigListenerMethod {

    private static void verifyMethodSignature(Method method) throws IllegalArgumentException {
        if (method.getParameterCount() == 0) {
            return;
        }

        if (method.getParameterCount() > 1) {
            throw new IllegalArgumentException(
                "method=" + method + " can only have ConfigInjectionContext as a parameter type, if any, as a ConfigListenerMethod");
        }

        final Class<?> paramType = method.getParameterTypes()[0];
        if (!ConfigInjectionContext.class.equals(paramType)) {
            throw new IllegalStateException(
                "method=" + method + " can only have ConfigInjectionContext as a parameter type as a ConfigListenerMethod");
        }
    }

    private final Method method;
    private final Set<ListenerType> types;
    private final boolean supplyContext;

    public ConfigListenerMethod(Method method, Iterable<ListenerType> types) throws IllegalArgumentException {
        Preconditions.checkNotNull(method, "method");
        ConfigListenerMethod.verifyMethodSignature(method);
        Preconditions.checkNotNull(types, "types");

        this.method = method;
        this.types = ImmutableSet.copyOf(types);
        this.supplyContext = method.getParameterCount() == 1;
    }

    public Method getMethod() {
        return this.method;
    }

    public Set<ListenerType> getTypes() {
        return this.types;
    }

    public boolean hasType(@Nullable ListenerType attribute) {
        return attribute != null && this.types.contains(attribute);
    }

    public boolean isSupplyContext() {
        return this.supplyContext;
    }

    public void invoke(Object object, ConfigInjectionContext context) throws InvocationTargetException, IllegalAccessException {
        this.method.setAccessible(true);
        if (this.supplyContext) {
            this.method.invoke(object, context);
        } else {
            this.method.invoke(object);
        }
    }

}