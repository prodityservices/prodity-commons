package io.prodity.commons.config.inject.listen;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.annotation.Nullable;

public class ConfigListenerMethod {

    private static void verifyMethodSignature(Method method) throws IllegalArgumentException {
        if (method.getParameters().length > 0) {
            throw new IllegalArgumentException("method=" + method.toString() + " can not have any parameters as a ConfigListenerMethod");
        }
    }

    private final Method method;
    private final Set<ListenerType> types;

    public ConfigListenerMethod(Method method, Iterable<ListenerType> types) throws IllegalArgumentException {
        Preconditions.checkNotNull(method, "method");
        ConfigListenerMethod.verifyMethodSignature(method);
        Preconditions.checkNotNull(types, "types");

        this.method = method;
        this.types = ImmutableSet.copyOf(types);
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

    public void invoke(Object object) throws InvocationTargetException, IllegalAccessException {
        this.method.setAccessible(true);
        this.method.invoke(object);
    }

}