package io.prodity.commons.spigot.legacy.item.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class YamlItemStack extends AbstractYamlType<ItemStack> {

    public static YamlItemStack get() {
        return YamlTypeCache.getType(YamlItemStack.class);
    }

    public YamlItemStack() {
        super(ItemStack.class, false);
    }

    @Override
    protected ItemStack loadInternally(ConfigurationSection section, String path) throws YamlException {
        final ImmutableItemBuilder builder = YamlImmutableItemBuilder.get().load(section, path);
        return builder.build();
    }

}