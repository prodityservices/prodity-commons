package io.prodity.commons.config.load.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.prodity.commons.config.load.element.ConfigElement;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

public class InjectableConfigMethod implements ConfigMethod {

    private final Method method;
    private final ImmutableList<ConfigElement> parameters;

    public InjectableConfigMethod(@Nonnull Method method, @Nonnull Iterable<ConfigElement> parameters) {
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(parameters, "parameters");
        this.method = method;
        this.parameters = ImmutableList.copyOf(parameters);
    }

    @Override
    @Nonnull
    public Method getMethod() {
        return this.method;
    }

    @Nonnull
    public ImmutableList<ConfigElement> getParameters() {
        return this.parameters;
    }

}