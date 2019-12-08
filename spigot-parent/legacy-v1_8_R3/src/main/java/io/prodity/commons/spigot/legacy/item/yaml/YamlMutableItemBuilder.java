package io.prodity.commons.spigot.legacy.item.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.MutableItemBuilder;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemBuilderMeta;
import java.util.Collection;

public class YamlMutableItemBuilder extends YamlItemBuilder<MutableItemBuilder> {

    public static YamlMutableItemBuilder get() {
        return YamlTypeCache.getType(YamlMutableItemBuilder.class);
    }

    public YamlMutableItemBuilder() {
        super(MutableItemBuilder.class);
    }

    @Override
    protected MutableItemBuilder createBuilder(ItemData data, Collection<ItemBuilderMeta<?>> meta) {
        return MutableItemBuilder.of(data, meta);
    }

}
