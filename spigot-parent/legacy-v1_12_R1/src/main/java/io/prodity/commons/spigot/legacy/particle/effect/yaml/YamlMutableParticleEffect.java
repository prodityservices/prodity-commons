package io.prodity.commons.spigot.legacy.particle.effect.yaml;

import io.prodity.commons.spigot.legacy.config.yaml.YamlException;
import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.MutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.offset.MutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note.YamlMutableParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note.YamlParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary.YamlMutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary.YamlParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.data.YamlMutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.data.YamlParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.offset.YamlMutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlMutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;
import org.bukkit.configuration.ConfigurationSection;

public class YamlMutableParticleEffect extends YamlParticleEffect<MutableParticleEffect> {

    public static YamlMutableParticleEffect get() {
        return YamlTypeCache.getType(YamlMutableParticleEffect.class);
    }

    public YamlMutableParticleEffect() {
        super(MutableParticleEffect.class);
    }

    @Override
    protected MutableOffsetParticleEffect loadOffsetEffect(ConfigurationSection section, String path) throws YamlException {
        return YamlMutableOffsetParticleEffect.get().load(section, path);
    }

    @Override
    protected ParticleFactory<MutableParticleEffect, ?, ?, ?> getFactory() {
        return ParticleEffects.mutable();
    }

    @Override
    protected YamlParticleColor<?> getParticleColorType() {
        return YamlMutableParticleColor.get();
    }

    @Override
    protected YamlParticleNoteColor<?> getParticleNoteColorType() {
        return YamlMutableParticleNoteColor.get();
    }

    @Override
    protected YamlParticleData<?> getParticleDataType() {
        return YamlMutableParticleData.get();
    }

    @Override
    protected YamlOptionalVector<?> getOptionalVectorType() {
        return YamlMutableOptionalVector.get();
    }

}