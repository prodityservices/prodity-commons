package io.prodity.commons.config.inject.member.method;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigFile;
import io.prodity.commons.config.inject.ConfigResolvable;
import io.prodity.commons.config.inject.deserialize.ConfigValueResolver;
import io.prodity.commons.config.inject.element.ConfigElementBase;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes.ColorizeAttribute;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.object.ConfigObject;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigMethodParameter<T> extends ConfigElementBase<T> implements ConfigResolvable {

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

    @Override
    public <V> Optional<V> getAttributeValue(Class<? extends ElementAttributeValue<V>> attributeClass) {
        if (!Objects.equals(ColorizeAttribute.class, attributeClass)) {
            return super.getAttributeValue(attributeClass);
        }
        final Optional<V> rawValue = (Optional<V>) super.getAttributeValue(ColorizeAttribute.class);
        return ConfigMember.isColorized(rawValue, this.possessor.isColorizeElements());
    }

    @Override
    public Object resolve(ConfigFile configFile, ConfigValueResolver valueResolver, ConfigurationNode node) throws ConfigInjectException {
        final Object value = valueResolver.resolveValue(this, node);
        return value;
    }

}