package io.prodity.commons.config.inject.object;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.prodity.commons.config.inject.listen.ConfigListener;
import io.prodity.commons.config.inject.listen.ConfigListenerMethod;
import io.prodity.commons.config.inject.listen.ConfigListenerResolver;
import io.prodity.commons.config.inject.listen.ListenerType;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class MasterConfigObject<T> extends ConfigObject<T> implements ConfigListener {

    public static <T> MasterConfigObject<T> of(Class<T> typeClass, T object) {
        Preconditions.checkNotNull(typeClass, "typeClass");
        Preconditions.checkNotNull(object, "object");

        final List<ConfigListenerMethod> listenerMethods = ConfigListenerResolver.resolveFromClass(typeClass);

        final MasterConfigObject<T> configObject = new MasterConfigObject<>(typeClass, object, listenerMethods);
        configObject.resolveMembers();

        return configObject;
    }

    private final List<ConfigListenerMethod> listenerMethods;

    public MasterConfigObject(Class<T> typeClass, T object, Iterable<ConfigListenerMethod> listenerMethods) {
        super(typeClass, object, false);
        this.listenerMethods = ImmutableList.copyOf(listenerMethods);
    }

    public List<ConfigListenerMethod> getListenerMethods() {
        return this.listenerMethods;
    }

    public List<ConfigListenerMethod> getListenerMethods(ListenerType type) {
        return this.listenerMethods.stream()
            .filter((method) -> method.hasType(type))
            .collect(Collectors.toList());
    }

    @Override
    public void callListeners(ListenerType type) throws InvocationTargetException, IllegalAccessException {
        final T object = this.getObject();
        for (ConfigListenerMethod method : this.getListenerMethods(type)) {
            method.invoke(object);
        }
    }

}