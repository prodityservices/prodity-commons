package io.prodity.commons.config.inject.member.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.inject.ConfigFile;
import io.prodity.commons.config.inject.deserialize.ConfigValueResolver;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.object.ConfigObject;
import io.prodity.commons.except.tryto.Try;
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
    public void inject(ConfigFile configFile, ConfigValueResolver valueResolver, ConfigurationNode node) throws ConfigInjectException {
        final List<Object> parameterValues = Lists.newArrayList();
        for (ConfigMethodParameter parameter : this.parameters) {
            final ConfigurationNode paramterNode = node.getNode(parameter.getPath());
            final Object value = parameter.resolve(configFile, valueResolver, paramterNode);
            parameterValues.add(value);
        }

        final Object object = this.possessor.getObject();

        this.method.setAccessible(true);
        Try.mapExceptionTo(() -> this.method.invoke(object, parameterValues.toArray()), ConfigInjectException.newMapper(configFile)).get();
    }

}