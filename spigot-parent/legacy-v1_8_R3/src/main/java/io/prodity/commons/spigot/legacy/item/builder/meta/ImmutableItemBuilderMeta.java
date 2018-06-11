package io.prodity.commons.spigot.legacy.item.builder.meta;

import io.prodity.commons.spigot.legacy.builder.meta.ImmutableBuilderMeta;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;

public interface ImmutableItemBuilderMeta<V> extends ItemBuilderMeta<V>, ImmutableBuilderMeta<V, ItemConstruction> {

    @Override
    default ImmutableItemBuilderMeta<V> toImmutable() {
        return this;
    }

}