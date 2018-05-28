package io.prodity.commons.spigot.legacy.item.builder.meta;

import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;

public interface ItemBuilderMeta<V> extends BuilderMeta<V, ItemConstruction> {

    @Override
    BuilderMetaKey<V, ItemConstruction, ? extends MutableItemBuilderMeta<V>, ? extends ImmutableItemBuilderMeta<V>> getKey();

    @Override
    default ImmutableItemBuilderMeta<V> toImmutable() {
        return this.getKey().toImmutable(this);
    }

    @Override
    default MutableItemBuilderMeta<V> toMutable() {
        return this.getKey().toMutable(this);
    }

}