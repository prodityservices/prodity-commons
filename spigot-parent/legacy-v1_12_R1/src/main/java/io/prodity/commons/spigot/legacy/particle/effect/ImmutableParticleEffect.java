package io.prodity.commons.spigot.legacy.particle.effect;

import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import java.util.Optional;

public interface ImmutableParticleEffect extends ParticleEffect {

    @Override
    ImmutableOptionalVector getOffsets();

    @Override
    Optional<ImmutableParticleColor> getColor();

    @Override
    Optional<ImmutableOptionalVector> getDirection();

    @Override
    Optional<ImmutableParticleData<?>> getData();

    @Override
    default ImmutableParticleData<?> convertData(ParticleData<?> data) throws IllegalArgumentException {
        return ParticleEffect.super.convertData(data).toImmutable();
    }

}