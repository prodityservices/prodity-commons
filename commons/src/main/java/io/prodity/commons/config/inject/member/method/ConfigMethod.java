package io.prodity.commons.config.inject.member.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.object.ConfigObject;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigMethod implements ConfigMember {

    public static List<ConfigMethod> fromObject(ConfigObject<?> configObject) {
        Preconditions.checkNotNull(configObject, "configObject");

        final List<ConfigMethod> methods = Lists.newArrayList();
        for (Method method : configObject.getTypeClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(ConfigInject.class)) {
                continue;
            }

            final List<ConfigMethodParameter<?>> parameters = ConfigMethodParameter.fromMethod(configObject, method);
            final ConfigMethod configMethod = new ConfigMethod(configObject, method, parameters);
            methods.add(configMethod);
        }

        return methods;
    }

    private final ConfigObject<?> possessor;
    private final Method method;
    private final List<ConfigMethodParameter<?>> parameters;

    public ConfigMethod(ConfigObject<?> possessor, Method method, Collection<ConfigMethodParameter<?>> parameters) {
        Preconditions.checkState(method.getParameterCount() == parameters.size(),
            "Method paramterCount=" + method.getParameterCount() + " not equal to specified ConfigMethodParamters size=" + parameters
                .size());
        this.possessor = possessor;
        this.method = method;
        this.parameters = ImmutableList.copyOf(parameters);
    }

    @Override
    public ConfigObject<?> getPossessor() {
        return this.possessor;
    }

    public Method getMethod() {
        return this.method;
    }

    public List<ConfigMethodParameter<?>> getParameters() {
        return this.parameters;
    }

    @Override
    public void inject(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        final List<Object> parameterValues = Lists.newArrayList();
        for (ConfigMethodParameter parameter : this.parameters) {
            final Object value = parameter.resolve(elementResolver, node);
            parameterValues.add(value);
        }

        final Object object = this.possessor.getObject();

        this.method.setAccessible(true);
        this.method.invoke(object, parameterValues.toArray());
    }

}