package io.prodity.commons.spigot.legacy.builder.meta;

import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import lombok.Getter;

public abstract class AbstractBuilderMeta<V, C extends BuilderConstruction<?>> implements BuilderMeta<V, C> {

    @Getter
    private final BuilderMetaKey<V, C, ?, ?> key;

    protected AbstractBuilderMeta(BuilderMetaKey<V, C, ?, ?> key) {
        this.key = key;
    }

    @Override
    public final void apply(C construction, V value) {
        if (this.canApply(construction)) {
            this.applyInternally(construction, value);
        }
    }

    protected abstract void applyInternally(C construction, V value);

}