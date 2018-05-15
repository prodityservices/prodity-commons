package io.prodity.commons.config.inject.member.method;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigResolvable;
import io.prodity.commons.config.inject.element.ConfigElementBase;
import io.prodity.commons.config.inject.object.ConfigObject;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigMethodParameter<T> extends ConfigElementBase<T> implements ConfigResolvable<T> {

    public static List<ConfigMethodParameter<?>> fromMethod(ConfigObject<?> configObject, Method method) {
        Preconditions.checkNotNull(configObject, "configObject");
        Preconditions.checkNotNull(method, "method");

        return Arrays.stream(method.getParameters())
            .map((parameter) -> new ConfigMethodParameter<>(configObject, parameter))
            .collect(Collectors.toList());
    }

    private static <T> TypeToken<T> getTypeToken(Parameter parameter) {
        Preconditions.checkNotNull(parameter, "parameter");
        return (TypeToken<T>) TypeToken.of(parameter.getParameterizedType());
    }

    private final ConfigObject<?> possessor;
    private final Parameter parameter;

    public ConfigMethodParameter(ConfigObject<?> possessor, Parameter parameter) {
        super(ConfigMethodParameter.getTypeToken(parameter), NamedAnnotatedElement.fromParameter(parameter));
        Preconditions.checkNotNull(possessor, "possessor");
        this.possessor = possessor;
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public ConfigObject<?> getPossessor() {
        return this.possessor;
    }

}