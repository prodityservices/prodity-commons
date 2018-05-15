package io.prodity.commons.config.inject.member.field;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.ConfigElementBase;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.object.ConfigObject;
import io.prodity.commons.reflect.element.NamedAnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
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
    public void inject(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        final T value = this.resolve(elementResolver, node);

        final Object object = this.possessor.getObject();

        this.field.setAccessible(true);
        FieldUtils.removeFinalModifier(this.field);
        this.field.set(object, value);
    }

}