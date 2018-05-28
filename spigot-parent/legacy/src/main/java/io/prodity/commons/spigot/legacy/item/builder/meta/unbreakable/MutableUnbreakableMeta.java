package io.prodity.commons.spigot.legacy.item.builder.meta.unbreakable;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class MutableUnbreakableMeta extends UnbreakableMeta implements MutableItemBuilderMeta<Boolean> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private Boolean value;

    public MutableUnbreakableMeta(boolean value) {
        this.value = value;
    }

}