package io.prodity.commons.config.inject.member;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class ConfigConstructor<T> extends ExecutableConfigMember {

    /**
     * Resolves the {@link ConfigConstructor} from the specified {@link Class}, if present.
     *
     * @param configClass the {@link Class} to resolve from
     * @param <T> the type
     * @return the resolved {@link ConfigConstructor}
     * @throws IllegalStateException if there is 0 or 2+ {@link ConfigConstructor}s for the specified class
     */
    public static <T> ConfigConstructor<T> fromClass(Class<T> configClass) throws IllegalStateException {
        Preconditions.checkNotNull(configClass, configClass);

        ConfigConstructor<T> noArgsConstructor = null;
        final List<ConfigConstructor<T>> constructors = Lists.newArrayList();

        for (Constructor<?> constructor : configClass.getDeclaredConstructors()) {
            final Constructor<T> typedConstructor = (Constructor<T>) constructor;
            if (constructor.getParameterCount() == 0) {
                noArgsConstructor = new ConfigConstructor<>(Lists.newArrayList(), typedConstructor);
            } else if (constructor.isAnnotationPresent(ConfigInject.class)) {
                final List<ConfigMemberParameter<?>> parameters = ConfigMemberParameter.fromExecutable(constructor);
                final ConfigConstructor<T> configConstructor = new ConfigConstructor<>(parameters, typedConstructor);
                constructors.add(configConstructor);
            }
        }

        if (constructors.isEmpty() && noArgsConstructor == null) {
            throw new IllegalStateException(
                "configClass=" + configClass + " has no Constructors with no paramters, or annotated with @ConfigInject");
        }

        if (constructors.size() > 1) {
            throw new IllegalStateException("configClass=" + configClass + " has multiple annotated marked with @ConfigInject");
        }

        return !constructors.isEmpty() ? constructors.get(0) : noArgsConstructor;
    }

    private final Constructor<T> constructor;
    private T objectInstance;

    public ConfigConstructor(Collection<ConfigMemberParameter<?>> parameters, Constructor<T> constructor) {
        super(parameters);
        Preconditions.checkNotNull(constructor, "constructor");
        this.constructor = constructor;
    }

    public Constructor<T> getMethod() {
        return this.constructor;
    }

    public T getObjectInstance() {
        return this.objectInstance;
    }

    @Override
    void apply(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        this.constructor.setAccessible(true);
        this.objectInstance = this.constructor.newInstance(parameters);
    }

}