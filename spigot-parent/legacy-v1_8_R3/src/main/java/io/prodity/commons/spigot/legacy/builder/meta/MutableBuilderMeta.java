package io.prodity.commons.spigot.legacy.builder.meta;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;

public interface MutableBuilderMeta<V, C extends BuilderConstruction<?>> extends BuilderMeta<V, C> {

    MutableBuilderMeta<V, C> setValue(V value);

    @Override
    default MutableBuilderMeta<V, C> toMutable() {
        return this;
    }

}