package io.prodity.commons.spigot.legacy.item.builder.meta.dyecolor;

import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.DyeColor;

public class MutableDyeColorMeta extends DyeColorMeta implements MutableItemBuilderMeta<DyeColor> {

    @Getter
    @Setter
    @Accessors(chain = true)
    private DyeColor value;

    public MutableDyeColorMeta(DyeColor value) {
        this.value = value;
    }

}
