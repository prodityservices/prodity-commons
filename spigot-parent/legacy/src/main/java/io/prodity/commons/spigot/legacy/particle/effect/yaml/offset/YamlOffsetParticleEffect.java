package io.prodity.commons.spigot.legacy.particle.effect.yaml.offset;

import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.offset.OffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;
import org.bukkit.configuration.ConfigurationSection;

public abstract class YamlOffsetParticleEffect<T extends OffsetParticleEffect> extends AbstractYamlType<T> {

    protected YamlOffsetParticleEffect(Class<T> clazz) {
        super(clazz, false);
    }

    @Override
    public T loadInternally(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection particleSection = YamlSection.get().load(section, path);

        final ParticleEffect effect = this.getParticleType().loadEffect(particleSection);

        final OptionalVector locationOffset;
        if (particleSection.contains(Keys.LOCATION_OFFSET)) {
            locationOffset = this.getOptionalVectorType().load(particleSection, Keys.LOCATION_OFFSET);
        } else {
            locationOffset = this.getFactory().createEmptyVector();
        }
        return this.getFactory().offsetEffect(effect, locationOffset);
    }

    protected abstract ParticleFactory<?, T, ?, ?> getFactory();

    protected abstract YamlOptionalVector<?> getOptionalVectorType();

    protected abstract YamlParticleEffect<?> getParticleType();

    public enum Keys {
        ;
        public static final String LOCATION_OFFSET = "location-offset";
    }

}