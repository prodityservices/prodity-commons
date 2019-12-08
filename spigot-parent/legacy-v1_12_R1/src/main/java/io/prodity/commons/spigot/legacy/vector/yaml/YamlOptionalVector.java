package io.prodity.commons.spigot.legacy.vector.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlDouble;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlOptionalVector<T extends OptionalVector> extends AbstractYamlType<T> {

    protected YamlOptionalVector(Class<T> clazz) {
        super(clazz, false);
    }

    private Optional<Double> loadComponent(ConfigurationSection section, String name) throws YamlException {
        if (!section.contains(name)) {
            return Optional.empty();
        }
        return Optional.ofNullable(YamlDouble.get().load(section, name));
    }

    @Override
    protected T loadInternally(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);
        final Optional<Double> x = this.loadComponent(section, "x");
        final Optional<Double> y = this.loadComponent(section, "y");
        final Optional<Double> z = this.loadComponent(section, "z");
        return this.createVector(x, y, z);
    }

    protected abstract T createVector(Optional<Double> x, Optional<Double> y, Optional<Double> z);

}