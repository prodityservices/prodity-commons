package io.prodity.commons.config.inject.member.field;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.ConfigFile;
import io.prodity.commons.config.inject.deserialize.ConfigValueResolver;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.ConfigElementBase;
import io.prodity.commons.config.inject.element.attribute.ElementAttributeValue;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes.ColorizeAttribute;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.object.ConfigObject;
import io.prodity.commons.except.tryto.Try;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ninja.leaping.configurate.ConfigurationNode;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ConfigField<T> extends ConfigElementBase<T> implements ConfigMember {

    public static List<ConfigField<?>> fromObject(ConfigObject<?> configObject) {
        Preconditions.checkNotNull(configObject, "configObject");

        final List<ConfigField<?>> fields = Lists.newArrayList();
        for (Field field : configObject.getTypeClass().getDeclaredFields()) {
            if (!ConfigElement.isElement(field)) {
                continue;
            }

            final ConfigField<?> configField = new ConfigField<>(configObject, field);
            fields.add(configField);
        }

        return fields;
    }

    private static <T> TypeToken<T> getTypeToken(Field field) {
        Preconditions.checkNotNull(field, "field");
        return (TypeToken<T>) TypeToken.of(field.getGenericType());
    }

    private final ConfigObject<?> possessor;
    private final Field field;

    public ConfigField(ConfigObject<?> possessor, Field field) {
        super(ConfigField.getTypeToken(field), NamedAnnotatedElement.fromField(field));
        Preconditions.checkNotNull(possessor, "possessor");
        this.possessor = possessor;
        this.field = field;
    }

    @Override
    public ConfigObject<?> getPossessor() {
        return this.possessor;
    }

    public Field getField() {
        return this.field;
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
    public void inject(ConfigFile configFile, ConfigValueResolver valueResolver, ConfigurationNode node) throws ConfigInjectException {
        final Object value = valueResolver.resolveValue(this, node);

        this.field.setAccessible(true);
        FieldUtils.removeFinalModifier(this.field);

        final Object object = this.possessor.getObject();
        Try.mapExceptionTo(() -> this.field.set(object, value), ConfigInjectException.newMapper(configFile)).run();
    }

}