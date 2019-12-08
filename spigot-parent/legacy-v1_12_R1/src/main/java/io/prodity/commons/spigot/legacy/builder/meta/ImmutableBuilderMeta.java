package io.prodity.commons.spigot.legacy.builder.meta;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;

public interface ImmutableBuilderMeta<V, C extends BuilderConstruction<?>> extends BuilderMeta<V, C> {

    @Override
    default ImmutableBuilderMeta<V, C> toImmutable() {
        return this;
    }

}
