package io.prodity.commons.spigot.legacy.vector.yaml;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlDouble;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class YamlVector extends AbstractYamlType<Vector> {

    public static final ImmutableSet<String> COMPONENTS = ImmutableSet.of("x", "y", "z");

    public static YamlVector get() {
        return YamlTypeCache.getType(YamlVector.class);
    }

    public YamlVector() {
        super(Vector.class, false);
    }

    @Override
    public Vector loadInternally(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection vectorSection = YamlSection.get().load(section, path);

        final double x = YamlDouble.get().load(vectorSection, "x");
        final double y = YamlDouble.get().load(vectorSection, "y");
        final double z = YamlDouble.get().load(vectorSection, "z");

        return new Vector(x, y, z);
    }

}