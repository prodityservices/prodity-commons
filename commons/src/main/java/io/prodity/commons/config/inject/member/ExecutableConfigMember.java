package io.prodity.commons.config.inject.member;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.config.inject.deserialize.ElementResolver;
import java.util.Collection;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;

public abstract class ExecutableConfigMember implements ConfigMember {

    private final List<ConfigMemberParameter<?>> parameters;

    protected ExecutableConfigMember(Collection<ConfigMemberParameter<?>> parameters) {
        Preconditions.checkNotNull(parameters, "parameters");

        this.parameters = ImmutableList.copyOf(parameters);
    }

    public List<ConfigMemberParameter<?>> getParameters() {
        return this.parameters;
    }

    @Override
    public void inject(ElementResolver elementResolver, ConfigurationNode node) throws Throwable {
        final List<Object> parameterValues = Lists.newArrayList();
        for (ConfigMemberParameter parameter : this.parameters) {
            final Object value = parameter.resolve(elementResolver, node);
            parameterValues.add(value);
        }

        this.apply(parameterValues.toArray());
    }

    abstract void apply(Object... parameters) throws Throwable;

}