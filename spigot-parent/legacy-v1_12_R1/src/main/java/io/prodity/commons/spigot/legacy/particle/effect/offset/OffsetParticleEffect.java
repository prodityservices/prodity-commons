package io.prodity.commons.spigot.legacy.particle.effect.offset;

import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;

public interface OffsetParticleEffect extends ParticleEffect {

    OptionalVector getLocationOffset();

}