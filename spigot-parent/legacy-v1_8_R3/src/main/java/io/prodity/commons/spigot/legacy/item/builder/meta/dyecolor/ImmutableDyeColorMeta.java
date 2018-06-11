package io.prodity.commons.spigot.legacy.item.builder.meta.dyecolor;

import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;
import org.bukkit.DyeColor;

public class ImmutableDyeColorMeta extends DyeColorMeta implements ImmutableItemBuilderMeta<DyeColor> {

    @Getter
    private final DyeColor value;

    protected ImmutableDyeColorMeta(DyeColor value) {
        this.value = value;
    }

}
