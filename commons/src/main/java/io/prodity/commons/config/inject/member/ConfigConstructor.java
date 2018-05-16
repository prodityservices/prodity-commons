package io.prodity.commons.config.inject.member;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigConstructor<T> extends ExecutableConfigMember {

    /**
     * Resolves the {@link ConfigConstructor} from the specified {@link Class}, if present.
     *
     * @param configClass the {@link Class} to resolve from
     * @param <T> the type
     * @return the resolved {@link ConfigConstructor}
     * @throws IllegalStateException if there is multiple or no {@link ConfigConstructor}s present.
     */
    public static <T> ConfigConstructor<T> fromClass(Class<T> configClass) throws IllegalStateException {
        Preconditions.checkNotNull(configClass, "configClass");

        final List<Constructor<?>> constructors = Arrays.stream(configClass.getDeclaredConstructors())
            .filter((constructor) -> !constructor.isSynthetic())
            .filter((constructor) -> constructor.isAnnotationPresent(ConfigInject.class))
            .collect(Collectors.toList());

        if (constructors.size() > 1) {
            throw new IllegalStateException(
                "configClass=" + configClass.getName() + "  has multiple constructors annotated with @ConfigInject");
        }

        if (constructors.isEmpty()) {
            try {
                final Constructor<?> constructor = configClass.getDeclaredConstructor();
                constructors.add(constructor);
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException(
                    "configClass=" + configClass.getName() + " has no constructors with 0 paramters or annotated with @ConfigInject");
            }
        }

        final Constructor<T> constructor = (Constructor<T>) constructors.get(0);
        final List<ConfigMemberParameter<?>> parameters = ConfigMemberParameter.fromExecutable(constructor);

        return new ConfigConstructor<>(constructor, parameters);
    }

    private final Constructor<T> constructor;
    private T objectInstance;

    public ConfigConstructor(Constructor<T> constructor, List<ConfigMemberParameter<?>> parameters) {
        super(parameters);
        Preconditions.checkNotNull(constructor, "constructor");
        this.constructor = constructor;
    }

    @Override
    void apply(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        this.constructor.setAccessible(true);
        this.objectInstance = this.constructor.newInstance(parameters);
    }

    public T instantiate(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        Preconditions.checkNotNull(elementResolver, "elementResolver");
        Preconditions.checkNotNull(node, "node");

        this.inject(elementResolver, node);
        return this.objectInstance;
    }

}