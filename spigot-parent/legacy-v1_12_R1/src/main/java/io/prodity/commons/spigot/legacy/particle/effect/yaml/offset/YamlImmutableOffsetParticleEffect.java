package io.prodity.commons.spigot.legacy.particle.effect.yaml.offset;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;
import io.prodity.commons.spigot.legacy.particle.effect.offset.ImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.yaml.YamlParticleEffect;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.yaml.YamlOptionalVector;

public class YamlImmutableOffsetParticleEffect extends YamlOffsetParticleEffect<ImmutableOffsetParticleEffect> {

    public static YamlImmutableOffsetParticleEffect get() {
        return YamlTypeCache.getType(YamlImmutableOffsetParticleEffect.class);
    }

    public YamlImmutableOffsetParticleEffect() {
        super(ImmutableOffsetParticleEffect.class);
    }

    @Override
    protected ParticleFactory<?, ImmutableOffsetParticleEffect, ?, ?> getFactory() {
        return ParticleEffects.immutable();
    }

    @Override
    protected YamlOptionalVector<?> getOptionalVectorType() {
        return YamlImmutableOptionalVector.get();
    }

    @Override
    protected YamlParticleEffect<?> getParticleType() {
        return YamlImmutableParticleEffect.get();
    }

}