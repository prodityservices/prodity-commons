package io.prodity.commons.spigot.legacy.item.builder.meta.unbreakable;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;

public class ImmutableUnbreakableMeta extends UnbreakableMeta implements ImmutableItemBuilderMeta<Boolean> {

    @Getter
    private final Boolean value;

    protected ImmutableUnbreakableMeta(boolean value) {
        this.value = value;
    }

}