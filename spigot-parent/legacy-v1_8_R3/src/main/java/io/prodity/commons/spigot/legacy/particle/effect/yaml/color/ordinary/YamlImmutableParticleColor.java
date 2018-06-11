package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlImmutableParticleColor extends YamlParticleColor<ImmutableParticleColor> {

    public static YamlImmutableParticleColor get() {
        return YamlTypeCache.getType(YamlImmutableParticleColor.class);
    }

    public YamlImmutableParticleColor() {
        super(ImmutableParticleColor.class);
    }

    @Override
    protected ParticleFactory<?, ?, ?, ImmutableParticleColor> getFactory() {
        return ParticleEffects.immutable();
    }

}