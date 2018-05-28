package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.ordinary;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlMutableParticleColor extends YamlParticleColor<MutableParticleColor> {

    public static YamlMutableParticleColor get() {
        return YamlTypeCache.getType(YamlMutableParticleColor.class);
    }

    public YamlMutableParticleColor() {
        super(MutableParticleColor.class);
    }

    @Override
    protected ParticleFactory<?, ?, ?, MutableParticleColor> getFactory() {
        return ParticleEffects.mutable();
    }

}