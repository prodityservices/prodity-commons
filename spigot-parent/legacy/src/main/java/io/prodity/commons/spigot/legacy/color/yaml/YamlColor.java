package io.prodity.commons.spigot.legacy.color.yaml;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlString;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlColor<T extends Color> extends AbstractYamlType<T> {

    public static final Set<String> COMPONENTS = ImmutableSet.of("red", "green", "blue");

    protected YamlColor(Class<T> colorClass) {
        super(colorClass, false);
    }

    public int loadComponent(ConfigurationSection section, String path) throws YamlException {
        final int value = YamlInt.get().load(section, path);
        if (value < 0 || value > 255) {
            throw new YamlException(section, path, "rgb value '" + path + "' = '" + value + "' must be 0-255 inclusive");
        }
        return value;
    }

    @Override
    protected T loadInternally(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);

        if (section.isString("hex")) {
            final String hex = YamlString.get().load(section, path);
            return this.parseHex(hex);
        }

        final YamlInt intType = YamlInt.get();
        final int red = this.loadComponent(section, "red");
        final int green = this.loadComponent(section, "green");
        final int blue = this.loadComponent(section, "blue");

        return this.parseRgb(red, green, blue);
    }

    abstract T parseHex(String hex);

    abstract T parseRgb(int red, int green, int blue);

}