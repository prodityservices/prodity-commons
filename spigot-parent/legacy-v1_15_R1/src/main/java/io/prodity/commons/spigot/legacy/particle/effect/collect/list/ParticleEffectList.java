package io.prodity.commons.spigot.legacy.particle.effect.collect.list;

import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.collect.ParticleEffectCollection;

import java.util.List;

public interface ParticleEffectList<T extends ParticleEffect> extends ParticleEffectCollection<T>, List<T> {

}
