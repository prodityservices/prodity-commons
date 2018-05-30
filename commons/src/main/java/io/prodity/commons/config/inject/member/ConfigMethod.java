package io.prodity.commons.config.inject.member;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.inject.ConfigObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class ConfigMethod extends ExecutableConfigMember {

    public static List<ConfigMethod> fromObject(ConfigObject<?> configObject) {
        Preconditions.checkNotNull(configObject, "configObject");

        final List<ConfigMethod> methods = Lists.newArrayList();
        for (Method method : configObject.getTypeClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(ConfigInject.class)) {
                continue;
            }

            final List<ConfigMemberParameter<?>> parameters = ConfigMemberParameter.fromExecutable(method);
            final ConfigMethod configMethod = new ConfigMethod(configObject, parameters, method);
            methods.add(configMethod);
        }

        return methods;
    }

    private final ConfigObject<?> possessor;
    private final Method method;

    public ConfigMethod(ConfigObject<?> possessor, Collection<ConfigMemberParameter<?>> parameters, Method method) {
        super(parameters);
        Preconditions.checkNotNull(possessor, "possessor");
        Preconditions.checkNotNull(method, "method");
        this.possessor = possessor;
        this.method = method;
    }

    @Override
    void apply(Object... parameters) throws InvocationTargetException, IllegalAccessException {
        this.method.setAccessible(true);
        this.method.invoke(this.possessor.getObjectInstance(), parameters);
    }

}