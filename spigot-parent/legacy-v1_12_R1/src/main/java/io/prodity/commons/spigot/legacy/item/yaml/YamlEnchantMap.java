package io.prodity.commons.spigot.legacy.item.yaml;

import com.google.common.collect.Maps;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

public class YamlEnchantMap extends AbstractYamlType<Map> {

    public static YamlEnchantMap get() {
        return YamlTypeCache.getType(YamlEnchantMap.class);
    }

    public YamlEnchantMap() {
        super(Map.class, false);
    }

    @Override
    protected Map<Enchantment, Integer> loadInternally(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);

        final Map<Enchantment, Integer> enchants = Maps.newHashMap();

        for (String enchantString : section.getKeys(false)) {
            final Enchantment enchantment = Enchantment.getByName(enchantString);
            if (enchantment == null) {
                throw new YamlException(section, path, "path is not a valid Enchantment");
            }

            final int level = YamlInt.get().load(section, enchantString);
            enchants.put(enchantment, level);
        }

        return enchants;
    }

}