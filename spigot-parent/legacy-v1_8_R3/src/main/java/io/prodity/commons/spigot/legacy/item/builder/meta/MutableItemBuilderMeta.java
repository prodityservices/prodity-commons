package io.prodity.commons.spigot.legacy.item.builder.meta;

import io.prodity.commons.spigot.legacy.builder.meta.MutableBuilderMeta;
import io.prodity.commons.spigot.legacy.item.builder.construct.ItemConstruction;

public interface MutableItemBuilderMeta<V> extends ItemBuilderMeta<V>, MutableBuilderMeta<V, ItemConstruction> {

    @Override
    MutableItemBuilderMeta<V> setValue(V value);

    @Override
    default MutableItemBuilderMeta<V> toMutable() {
        return this;
    }

}