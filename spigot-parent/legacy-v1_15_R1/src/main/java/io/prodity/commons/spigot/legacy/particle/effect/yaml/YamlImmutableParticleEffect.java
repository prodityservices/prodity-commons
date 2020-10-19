package io.prodity.commons.spigot.legacy.particle.effect.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.offset.ImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note.YamlImmutableParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note.YamlParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary.YamlImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary.YamlParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.data.YamlImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.data.YamlParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.offset.YamlImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;
import org.bukkit.configuration.ConfigurationSection;

public class YamlImmutableParticleEffect extends YamlParticleEffect<ImmutableParticleEffect> {

    public static YamlImmutableParticleEffect get() {
        return YamlTypeCache.getType(YamlImmutableParticleEffect.class);
    }

    public YamlImmutableParticleEffect() {
        super(ImmutableParticleEffect.class);
    }

    @Override
    protected ImmutableOffsetParticleEffect loadOffsetEffect(ConfigurationSection section, String path) throws YamlException {
        return YamlImmutableOffsetParticleEffect.get().load(section, path);
    }

    @Override
    protected ParticleFactory<ImmutableParticleEffect, ?, ?, ?> getFactory() {
        return ParticleEffects.immutable();
    }

    @Override
    protected YamlParticleColor<?> getParticleColorType() {
        return YamlImmutableParticleColor.get();
    }

    @Override
    protected YamlParticleNoteColor<?> getParticleNoteColorType() {
        return YamlImmutableParticleNoteColor.get();
    }

    @Override
    protected YamlParticleData<?> getParticleDataType() {
        return YamlImmutableParticleData.get();
    }

    @Override
    protected YamlOptionalVector<?> getOptionalVectorType() {
        return YamlImmutableOptionalVector.get();
    }

}