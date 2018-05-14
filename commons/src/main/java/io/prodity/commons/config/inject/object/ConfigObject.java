package io.prodity.commons.config.inject.object;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.inject.ConfigFile;
import io.prodity.commons.config.inject.ConfigInjectable;
import io.prodity.commons.config.inject.deserialize.ConfigValueResolver;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.config.inject.member.ConfigMember;
import io.prodity.commons.config.inject.member.field.ConfigField;
import io.prodity.commons.config.inject.member.method.ConfigMethod;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;

public class ConfigObject<T> implements ConfigInjectable {

    public static List<ConfigMember> resolveMembers(ConfigObject<?> configObject) {
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
    private final boolean colorizeElements;

    public ConfigObject(Class<T> typeClass, T object, boolean colorizeElements) {
        this.typeClass = typeClass;
        this.object = object;
        this.colorizeElements = colorizeElements;
    }

    public Class<T> getTypeClass() {
        return this.typeClass;
    }

    public T getObject() {
        return this.object;
    }

    void resolveMembers() throws IllegalStateException {
        if (this.members != null) {
            throw new IllegalStateException(this.toString() + " already had its members resolved");
        }
        this.members = ImmutableList.copyOf(ConfigObject.resolveMembers(this));
    }

    public List<ConfigMember> getMembers() {
        if (this.members == null) {
            this.resolveMembers();
        }
        return this.members;
    }

    public boolean isColorizeElements() {
        return this.colorizeElements;
    }

    @Override
    public void inject(ConfigFile configFile, ConfigValueResolver valueResolver, ConfigurationNode node) throws ConfigInjectException {
        //TODO
    }

}