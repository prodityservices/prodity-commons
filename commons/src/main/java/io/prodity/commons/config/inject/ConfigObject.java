package io.prodity.commons.config.inject;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.inject.listen.ConfigListener;
import io.prodity.commons.config.inject.listen.ConfigListenerMethod;
import io.prodity.commons.config.inject.listen.ConfigListenerResolver;
import io.prodity.commons.config.inject.listen.ListenerType;
import io.prodity.commons.config.inject.member.ConfigConstructor;
import io.prodity.commons.config.inject.member.ConfigField;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.member.ConfigMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigObject<T> implements ConfigInjectable, ConfigListener {

    public static <T> ConfigObject<T> of(Class<T> typeClass) throws IllegalStateException {
        Preconditions.checkNotNull(typeClass, "typeClass");

        final ConfigConstructor<T> constructor = ConfigConstructor.fromClass(typeClass);
        final List<ConfigListenerMethod> listenerMethods = ConfigListenerResolver.resolveFromClass(typeClass);
        final ConfigObject<T> configObject = new ConfigObject<>(typeClass, constructor, listenerMethods);
        configObject.resolveMembers();

        return configObject;
    }

    protected static List<ConfigMember> resolveMembers(ConfigObject<?> configObject) {
        Preconditions.checkNotNull(configObject, "configObject");

        final List<ConfigMember> members = Lists.newArrayList();

        final List<ConfigField<?>> fields = ConfigField.fromObject(configObject);
        members.addAll(fields);

        final List<ConfigMethod> methods = ConfigMethod.fromObject(configObject);
        members.addAll(methods);

        return members;
    }

    private final Class<T> typeClass;
    private final ConfigConstructor<T> constructor;
    private final List<ConfigListenerMethod> listenerMethods;
    private List<ConfigMember> members;
    private T objectInstance;

    protected ConfigObject(Class<T> typeClass, ConfigConstructor<T> constructor, Iterable<ConfigListenerMethod> listenerMethods) {
        Preconditions.checkNotNull(typeClass, "typeClass");
        Preconditions.checkNotNull(constructor, "constructor");
        Preconditions.checkNotNull(listenerMethods, "listenerMethods");

        this.typeClass = typeClass;
        this.constructor = constructor;
        this.listenerMethods = ImmutableList.copyOf(listenerMethods);
    }

    public Class<T> getTypeClass() {
        return this.typeClass;
    }

    @Nullable
    public T getObjectInstance() {
        return this.objectInstance;
    }

    private void resolveMembers() {
        if (this.members == null) {
            this.members = ImmutableList.copyOf(ConfigObject.resolveMembers(this));
        }
    }

    public List<ConfigMember> getMembers() {
        this.resolveMembers();
        return this.members;
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
    public void callListeners(ListenerType type, ConfigInjectionContext context)
        throws IllegalStateException, InvocationTargetException, IllegalAccessException {
        if (this.objectInstance == null) {
            throw new IllegalStateException("inner object instance has not yet been instantiated");
        }
        for (ConfigListenerMethod method : this.getListenerMethods(type)) {
            method.invoke(this.objectInstance, context);
        }
    }

    @Override
    public void inject(ConfigInjectionContext context, ConfigurationNode node) throws Throwable {
        this.constructor.inject(context, node);

        this.objectInstance = this.constructor.instantiate(context, node);

        this.callListeners(ListenerType.PRE_INJECT, context);

        context.getServiceLocator().inject(this.objectInstance);

        for (ConfigMember member : this.getMembers()) {
            member.inject(context, node);
        }

        this.callListeners(ListenerType.POST_INJECT, context);
    }

}