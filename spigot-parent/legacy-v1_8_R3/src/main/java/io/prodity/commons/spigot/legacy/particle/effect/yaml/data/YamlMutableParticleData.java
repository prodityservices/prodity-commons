package io.prodity.commons.spigot.legacy.particle.effect.yaml.data;

import io.prodity.commons.spigot.legacy.config.yaml.YamlTypeCache;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.factory.ParticleFactory;

public class YamlMutableParticleData extends YamlParticleData<MutableParticleData> {

    public static YamlMutableParticleData get() {
        return YamlTypeCache.getType(YamlMutableParticleData.class);
    }

    public YamlMutableParticleData() {
        super(MutableParticleData.class);
    }

    @Override
    protected ParticleFactory<?, ?, ? extends MutableParticleData, ?> getFactory() {
        return ParticleEffects.mutable();
    }

}