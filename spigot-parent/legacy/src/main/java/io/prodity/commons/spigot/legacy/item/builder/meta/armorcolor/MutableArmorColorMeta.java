package io.prodity.commons.spigot.legacy.item.builder.meta.armorcolor;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.MutableColor;
import io.prodity.commons.spigot.legacy.item.builder.meta.MutableItemBuilderMeta;
import lombok.Getter;
import lombok.experimental.Accessors;

public class MutableArmorColorMeta extends ArmorColorMeta implements MutableItemBuilderMeta<Color> {

    @Getter
    @Accessors(chain = true)
    private MutableColor value;

    public MutableArmorColorMeta(Color value) {
        this.value = Colors.mutableFrom(value);
    }

    @Override
    public MutableArmorColorMeta setValue(Color value) {
        this.value = Colors.mutableFrom(value);
        return this;
    }

}