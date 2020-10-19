package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlMutableParticleNoteColor extends YamlParticleNoteColor<MutableParticleColor> {

    public static YamlMutableParticleNoteColor get() {
        return YamlTypeCache.getType(YamlMutableParticleNoteColor.class);
    }

    public YamlMutableParticleNoteColor() {
        super(MutableParticleColor.class);
    }

    @Override
    protected ParticleFactory<?, ?, ?, MutableParticleColor> getFactory() {
        return ParticleEffects.mutable();
    }

}