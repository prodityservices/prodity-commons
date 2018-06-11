package io.prodity.commons.spigot.legacy.item.builder.meta;

import io.prodity.commons.spigot.legacy.builder.meta.AbstractBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;

public abstract class AbstractItemBuilderMeta<V> extends AbstractBuilderMeta<V, ItemConstruction> implements ItemBuilderMeta<V> {

    protected AbstractItemBuilderMeta(BuilderMetaKey<V, ItemConstruction, ?, ?> key) {
        super(key);
    }

    @Override
    public BuilderMetaKey<V, ItemConstruction, ? extends MutableItemBuilderMeta<V>, ? extends ImmutableItemBuilderMeta<V>> getKey() {
        return (BuilderMetaKey<V, ItemConstruction, ? extends MutableItemBuilderMeta<V>, ? extends ImmutableItemBuilderMeta<V>>) super
            .getKey();
    }

}