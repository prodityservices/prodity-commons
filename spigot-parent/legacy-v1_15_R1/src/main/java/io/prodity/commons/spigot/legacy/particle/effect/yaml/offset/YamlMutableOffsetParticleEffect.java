package io.prodity.commons.spigot.legacy.particle.effect.yaml.offset;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.offset.MutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlMutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlParticleEffect;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlMutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;

public class YamlMutableOffsetParticleEffect extends YamlOffsetParticleEffect<MutableOffsetParticleEffect> {

    public static YamlMutableOffsetParticleEffect get() {
        return YamlTypeCache.getType(YamlMutableOffsetParticleEffect.class);
    }

    public YamlMutableOffsetParticleEffect() {
        super(MutableOffsetParticleEffect.class);
    }

    @Override
    protected ParticleFactory<?, MutableOffsetParticleEffect, ?, ?> getFactory() {
        return ParticleEffects.mutable();
    }

    @Override
    protected YamlOptionalVector<?> getOptionalVectorType() {
        return YamlMutableOptionalVector.get();
    }

    @Override
    protected YamlParticleEffect<?> getParticleType() {
        return YamlMutableParticleEffect.get();
    }

}