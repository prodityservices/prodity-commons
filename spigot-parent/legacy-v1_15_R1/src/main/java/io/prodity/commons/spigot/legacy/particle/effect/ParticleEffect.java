package io.prodity.commons.spigot.legacy.particle.effect;

import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import org.bukkit.Particle;

import java.util.Optional;

public interface ParticleEffect extends ParticleEffectPlayer {

    Particle getParticle();

    int getAmount();

    OptionalVector getOffsets();

    Optional<? extends ParticleColor> getColor();

    Optional<? extends OptionalVector> getDirection();

    Optional<Double> getSpeed();

    Optional<? extends ParticleData<?>> getData();

    default ParticleData<?> convertData(ParticleData<?> data) throws IllegalArgumentException {
        return ParticleEffects.convertData(this.getParticle(), data);
    }

}