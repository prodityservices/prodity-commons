package io.prodity.commons.spigot.legacy.particle.effect.yaml.data;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlByte;
import io.prodity.commons.spigot.legacy.item.yaml.YamlMaterial;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlParticleData<T extends ParticleData> extends AbstractYamlType<T> {

    protected YamlParticleData(Class<T> clazz) {
        super(clazz, false);
    }

    @Override
    protected T loadInternally(ConfigurationSection section, String path) throws YamlException {
        section = YamlSection.get().load(section, path);
        final Material material = YamlMaterial.get().load(section, "material");
        if (section.contains("data")) {
            final byte data = YamlByte.get().load(section, "data");
            return this.getFactory().itemData(material, data);
        } else {
            return this.getFactory().itemData(material);
        }
    }

    protected abstract ParticleFactory<?, ?, ? extends T, ?> getFactory();

}