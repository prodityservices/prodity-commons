package io.prodity.commons.spigot.legacy.color.yaml;

import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.MutableColor;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;

public class YamlMutableColor extends YamlColor<MutableColor> {

    public static YamlMutableColor get() {
        return YamlTypeCache.getType(YamlMutableColor.class);
    }

    public YamlMutableColor() {
        super(MutableColor.class);
    }

    @Override
    MutableColor parseHex(String hex) {
        return Colors.mutableFromHex(hex);
    }

    @Override
    MutableColor parseRgb(int red, int green, int blue) {
        return Colors.mutableFromRgb(red, green, blue);
    }

}