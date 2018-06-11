package io.prodity.commons.spigot.legacy.particle.effect.offset;

import io.prodity.commons.spigot.legacy.particle.effect.ImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;

public interface ImmutableOffsetParticleEffect extends OffsetParticleEffect, ImmutableParticleEffect {

    @Override
    ImmutableOptionalVector getLocationOffset();

}