package io.prodity.commons.spigot.legacy.item.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import io.prodity.commons.spigot.legacy.tryto.Try;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class YamlMaterial extends AbstractYamlType<Material> {

    public static YamlMaterial get() {
        return YamlTypeCache.getType(YamlMaterial.class);
    }

    public YamlMaterial() {
        super(Material.class, true);
    }

    @Override
    public void verify(ConfigurationSection section, String path) throws YamlException {
        final String string = YamlString.get().load(section, path);
        if (Material.getMaterial(string) == null) {
            throw new YamlException(section, path, "path is not a valid Material");
        }
    }

    @Override
    public Material loadInternally(ConfigurationSection section, String path) throws YamlException {
        final String string = Try.biFunction(YamlString.get()::load).apply(section, path);
        return Material.getMaterial(string);
    }

}