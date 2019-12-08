package io.prodity.commons.spigot.legacy.item.repo;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.item.yaml.YamlImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import java.io.File;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemRepository extends YamlRepository<ImmutableItemBuilder> {

    public ItemRepository(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public ItemRepository(File file) {
        super(file);
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        return section.isConfigurationSection(path) && section.getConfigurationSection(path).isString("material");
    }

    @Override
    protected ImmutableItemBuilder load(ConfigurationSection section, String path) throws YamlException {
        return YamlImmutableItemBuilder.get().load(section, path);
    }

    public ItemStack getItem(String key) {
        return this.get(key).build();
    }

    public ItemStack build(String key, Replacer replacer) {
        return this.get(key).build(replacer);
    }

    public ItemStack build(String key, Replacer replacer, Player player) {
        return this.get(key).build(player, replacer);
    }

    public ItemStack papiBuild(String key, Player player) {
        return this.get(key).papiBuild(player);
    }

    public ItemStack papiBuild(String key, Replacer replacer, Player player) {
        return this.get(key).papiBuild(player, replacer);
    }

}