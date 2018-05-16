package io.prodity.commons.config.inject.member;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigResolvable;
import io.prodity.commons.config.inject.element.ConfigElementBase;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigMemberParameter<T> extends ConfigElementBase<T> implements ConfigResolvable<T> {

    public static List<ConfigMemberParameter<?>> fromExecutable(Executable executable) {
        Preconditions.checkNotNull(executable, "executable");

        return Arrays.stream(executable.getParameters())
            .map(ConfigMemberParameter::new)
            .collect(Collectors.toList());
    }

    private static <T> TypeToken<T> getTypeToken(Parameter parameter) {
        Preconditions.checkNotNull(parameter, "parameter");
        return (TypeToken<T>) TypeToken.of(parameter.getParameterizedType());
    }

    private final Parameter parameter;

    public ConfigMemberParameter(Parameter parameter) {
        super(ConfigMemberParameter.getTypeToken(parameter), NamedAnnotatedElement.fromParameter(parameter));
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

}