package io.prodity.commons.spigot.legacy.color.yaml;

import io.prodity.commons.spigot.legacy.color.Colors;
import io.prodity.commons.spigot.legacy.color.ImmutableColor;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;

public class YamlImmutableColor extends YamlColor<ImmutableColor> {

    public static YamlImmutableColor get() {
        return YamlTypeCache.getType(YamlImmutableColor.class);
    }

    public YamlImmutableColor() {
        super(ImmutableColor.class);
    }

    @Override
    ImmutableColor parseHex(String hex) {
        return Colors.immutableFromHex(hex);
    }

    @Override
    ImmutableColor parseRgb(int red, int green, int blue) {
        return Colors.immutableFromRgb(red, green, blue);
    }

}