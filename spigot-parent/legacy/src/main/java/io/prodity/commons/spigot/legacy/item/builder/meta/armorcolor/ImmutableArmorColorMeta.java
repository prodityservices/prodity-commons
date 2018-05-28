package io.prodity.commons.spigot.legacy.item.builder.meta.armorcolor;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.ImmutableColor;
import io.prodity.commons.spigot.legacy.item.builder.meta.ImmutableItemBuilderMeta;
import lombok.Getter;

public class ImmutableArmorColorMeta extends ArmorColorMeta implements ImmutableItemBuilderMeta<Color> {

    @Getter
    private final ImmutableColor value;

    protected ImmutableArmorColorMeta(Color value) {
        this.value = Colors.immutableFrom(value);
    }

}