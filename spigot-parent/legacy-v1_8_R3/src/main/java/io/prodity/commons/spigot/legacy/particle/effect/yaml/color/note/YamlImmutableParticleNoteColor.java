package io.prodity.commons.spigot.legacy.particle.effect.yaml.color.note;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlImmutableParticleNoteColor extends YamlParticleNoteColor<ImmutableParticleColor> {

    public static YamlImmutableParticleNoteColor get() {
        return YamlTypeCache.getType(YamlImmutableParticleNoteColor.class);
    }

    public YamlImmutableParticleNoteColor() {
        super(ImmutableParticleColor.class);
    }

    @Override
    protected ParticleFactory<?, ?, ?, ImmutableParticleColor> getFactory() {
        return ParticleEffects.immutable();
    }

}
