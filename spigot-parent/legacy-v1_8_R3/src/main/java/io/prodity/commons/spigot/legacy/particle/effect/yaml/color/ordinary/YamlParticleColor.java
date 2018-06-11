package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.color.yaml.YamlImmutableColor;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlParticleColor<T extends ParticleColor> extends AbstractYamlType<T> {

    protected YamlParticleColor(Class<T> clazz) {
        super(clazz, false);
    }

    @Override
    protected T loadInternally(ConfigurationSection section, String path) throws YamlException {
        final Color color = YamlImmutableColor.get().load(section, path);
        return this.getFactory().ordinaryColor(color);
    }

    protected abstract ParticleFactory<?, ?, ?, T> getFactory();

}