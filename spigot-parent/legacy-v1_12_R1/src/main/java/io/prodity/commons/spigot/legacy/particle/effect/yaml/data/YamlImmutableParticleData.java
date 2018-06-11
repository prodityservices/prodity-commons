package io.prodity.commons.spigot.legacy.particle.effect.yaml.data;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlImmutableParticleData extends YamlParticleData<ImmutableParticleData> {

    public static YamlImmutableParticleData get() {
        return YamlTypeCache.getType(YamlImmutableParticleData.class);
    }

    public YamlImmutableParticleData() {
        super(ImmutableParticleData.class);
    }

    @Override
    protected ParticleFactory<?, ?, ? extends ImmutableParticleData, ?> getFactory() {
        return ParticleEffects.immutable();
    }

}