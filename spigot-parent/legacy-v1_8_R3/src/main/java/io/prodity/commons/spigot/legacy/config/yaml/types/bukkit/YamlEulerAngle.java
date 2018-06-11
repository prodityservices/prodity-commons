package io.prodity.commons.spigot.legacy.config.yaml.types.bukkit;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlVector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class YamlEulerAngle extends AbstractYamlType<EulerAngle> {

    public static YamlEulerAngle get() {
        return YamlTypeCache.getType(YamlEulerAngle.class);
    }

    public YamlEulerAngle() {
        super(EulerAngle.class, false);
    }

    @Override
    public EulerAngle loadInternally(ConfigurationSection section, String path) throws YamlException {
        final Vector vector = YamlVector.get().load(section, path);
        return new EulerAngle(vector.getX(), vector.getY(), vector.getZ());
    }

}