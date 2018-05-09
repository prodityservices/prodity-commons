package io.prodity.commons.config.load.method;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public interface ConfigMethod {

    Method getMethod();

    default void invoke(@Nonnull Object object, Object... arguments)
        throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Preconditions.checkNotNull(object, "object");

        final Method method = this.getMethod();
        method.setAccessible(true);

        final int parameterCount = method.getParameterCount();
        if (parameterCount != arguments.length) {
            throw new IllegalArgumentException(
                "method=" + method.toString() + " has parameterCount=" + parameterCount + " but specified arguments.length="
                    + arguments.length);
        }
        
        method.invoke(object, arguments);
    }

}