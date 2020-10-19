package io.prodity.commons.spigot.legacy.particle.effect.yaml;

import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.config.yaml.AbstractYamlType;
import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.types.bukkit.YamlSection;
import io.prodity.commons.spigot.legacy.config.yaml.types.enumeration.YamlEnum;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlDouble;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlInt;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note.YamlParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary.YamlParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.data.YamlParticleData;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public abstract class YamlParticleEffect<T extends ParticleEffect> extends AbstractYamlType<T> {

    protected YamlParticleEffect(Class<T> clazz) {
        super(clazz, false);
    }

    private void verifyNoConflicting(ConfigurationSection section, String... keys) throws YamlException {
        final ImmutableMap.Builder<String, Boolean> mapBuilder = ImmutableMap.builder();
        for (String key : keys) {
            mapBuilder.put(key, section.contains(key));
        }
        final Map<String, Boolean> conflicting = mapBuilder.build();

        int count = 0;
        for (Entry<String, Boolean> entry : conflicting.entrySet()) {
            if (entry.getValue()) {
                count++;
            }
            if (count > 1) {
                final String keyString = String.join(",", keys);
                throw new YamlException(section,
                    "one or more conflicting particle attributes were specified (" + keys + ")");
            }
        }
    }

    private void verifyAmount(ConfigurationSection section) throws YamlException {
        if (section.contains(Keys.AMOUNT)) {
            final double amount = YamlDouble.get().loadInternally(section, Keys.AMOUNT);
            if (amount < 0) {
                throw new YamlException(section, Keys.AMOUNT + "=" + amount + " must be >=0");
            }
        }
    }

    @Override
    public T loadInternally(ConfigurationSection section, String path) throws YamlException {
        final ConfigurationSection particleSection = YamlSection.get().load(section, path);

        if (particleSection.contains("location-offset")) {
            return this.loadOffsetEffect(section, path);
        }

        return this.loadEffect(particleSection);
    }

    @Deprecated
    public T loadEffect(ConfigurationSection section) throws YamlException {
        final Particle particle = YamlEnum.get(Particle.class).load(section, Keys.PARTICLE);
        this.verifyAmount(section);

        // Colorized - particle, color
        if (section.contains(Keys.COLOR) || section.contains(Keys.NOTE_COLOR)) {
            this.verifyNoConflicting(section, Keys.DIRECTION, Keys.SPEED, Keys.AMOUNT, Keys.OFFSET, Keys.ITEM_DATA);

            final boolean ordinaryColor = section.contains(Keys.COLOR);
            final ParticleColor color;

            if (ordinaryColor) {
                this.verifyNoConflicting(section, Keys.NOTE_COLOR);
                color = this.getParticleNoteColorType().load(section, Keys.NOTE_COLOR);
            } else {
                this.verifyNoConflicting(section, Keys.COLOR);
                color = this.getParticleColorType().load(section, Keys.COLOR);
            }

            return this.getFactory().effect(particle, color);
        }

        final Optional<Double> speed = YamlDouble.get().loadOptional(section, Keys.SPEED);
        final Optional<? extends ParticleData> data = this.getParticleDataType().loadOptional(section, Keys.ITEM_DATA);

        // Direction
        if (section.contains(Keys.DIRECTION)) {
            this.verifyNoConflicting(section, Keys.AMOUNT, Keys.OFFSET, Keys.COLOR, Keys.NOTE_COLOR);

            final OptionalVector direction = this.getOptionalVectorType().load(section, Keys.DIRECTION);
            return this.getFactory().effect(particle, data.orElse(null), direction, speed.orElse(null));
        }

        // Generic
        this.verifyNoConflicting(section, Keys.DIRECTION, Keys.COLOR,
            Keys.NOTE_COLOR);

        final Optional<Integer> amount = YamlInt.get().loadOptional(section, Keys.AMOUNT);
        final OptionalVector offsets;

        if (section.contains(Keys.OFFSET)) {
            offsets = this.getOptionalVectorType().load(section, Keys.OFFSET);
        } else {
            offsets = this.getFactory().createEmptyVector();
        }

        return this.getFactory().effect(particle, amount.orElse(1), offsets, speed.orElse(null), data.orElse(null));
    }

    protected abstract <T1 extends T> T1 loadOffsetEffect(ConfigurationSection section, String path) throws YamlException;

    protected abstract ParticleFactory<T, ?, ?, ?> getFactory();

    protected abstract YamlParticleColor<?> getParticleColorType();

    protected abstract YamlParticleNoteColor<?> getParticleNoteColorType();

    protected abstract YamlParticleData<?> getParticleDataType();

    protected abstract YamlOptionalVector<?> getOptionalVectorType();

    public enum Keys {
        ;
        public static final String PARTICLE = "particle";
        public static final String AMOUNT = "amount";
        public static final String ITEM_DATA = "item-data";
        public static final String SPEED = "speed";
        public static final String DIRECTION = "direction";
        public static final String COLOR = "color";
        public static final String NOTE_COLOR = "note-color";
        public static final String OFFSET = "offset";
    }

}