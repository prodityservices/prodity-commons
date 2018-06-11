package io.prodity.commons.spigot.legacy.item.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.item.ItemData;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.item.builder.meta.ItemBuilderMeta;
import java.util.Collection;

public class YamlImmutableItemBuilder extends YamlItemBuilder<ImmutableItemBuilder> {

    public static YamlImmutableItemBuilder get() {
        return YamlTypeCache.getType(YamlImmutableItemBuilder.class);
    }

    public YamlImmutableItemBuilder() {
        super(ImmutableItemBuilder.class);
    }

    @Override
    protected ImmutableItemBuilder createBuilder(ItemData data, Collection<ItemBuilderMeta<?>> meta) {
        return ImmutableItemBuilder.of(data, meta);
    }

}
