package io.prodity.commons.config.inject.object;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.inject.ConfigInjectable;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.member.field.ConfigField;
import io.prodity.commons.config.inject.member.method.ConfigMethod;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigObject<T> implements ConfigInjectable {

    public static <T> ConfigObject<T> of(Class<T> typeClass, T object) {
        Preconditions.checkNotNull(typeClass, "typeClass");
        Preconditions.checkNotNull(object, "object");

        final ConfigObject<T> configObject = new ConfigObject<>(typeClass, object);
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
    private final T object;
    private List<ConfigMember> members;

    protected ConfigObject(Class<T> typeClass, T object) {
        this.typeClass = typeClass;
        this.object = object;
    }

    public Class<T> getTypeClass() {
        return this.typeClass;
    }

    public T getObject() {
        return this.object;
    }

    void resolveMembers() {
        if (this.members == null) {
            this.members = ImmutableList.copyOf(ConfigObject.resolveMembers(this));
        }
    }

    public List<ConfigMember> getMembers() {
        this.resolveMembers();
        return this.members;
    }

    @Override
    public void inject(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        for (ConfigMember member : this.getMembers()) {
            member.inject(elementResolver, node);
        }
    }

}