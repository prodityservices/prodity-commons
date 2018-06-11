package io.prodity.commons.spigot.legacy.builder.meta;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;

public interface BuilderMeta<V, C extends BuilderConstruction<?>> {

    BuilderMetaKey<V, C, ?, ?> getKey();

    V getValue();

    boolean canApply(C construction);

    void apply(C construction, V value);

    default ImmutableBuilderMeta<V, C> toImmutable() {
        return this.getKey().toImmutable(this);
    }

    default MutableBuilderMeta<V, C> toMutable() {
        return this.getKey().toMutable(this);
    }

}
