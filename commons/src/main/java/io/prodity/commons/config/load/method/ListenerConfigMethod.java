package io.prodity.commons.config.load.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ListenerConfigMethod implements ConfigMethod {

    private final Method method;
    private final ImmutableSet<ListenerType> types;

    public ListenerConfigMethod(@Nonnull Method method, @Nonnull Set<ListenerType> attributes) {
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(attributes, "types");
        this.method = method;
        this.types = ImmutableSet.copyOf(attributes);
    }

    @Override
    @Nonnull
    public Method getMethod() {
        return this.method;
    }

    @Nonnull
    public ImmutableSet<ListenerType> getTypes() {
        return this.types;
    }

    public boolean hasType(@Nullable ListenerType attribute) {
        return attribute != null && this.types.contains(attribute);
    }

}