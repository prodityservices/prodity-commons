package io.prodity.commons.spigot.legacy.particle.effect;

import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import java.util.Optional;
import java.util.function.Function;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public interface MutableParticleEffect extends ParticleEffect {

    @Override
    ImmutableOptionalVector getOffsets();

    default MutableParticleEffect setOffsets(Vector vector) {
        return this.setOffsets(MutableOptionalVector.fromVector(vector));
    }

    MutableParticleEffect setOffsets(OptionalVector vector);

    @Override
    Optional<ImmutableParticleColor> getColor();

    default MutableParticleEffect setColor(ParticleColor color) {
        return this.setColor(Optional.ofNullable(color));
    }

    MutableParticleEffect setColor(Optional<ParticleColor> color);

    @Override
    Optional<ImmutableOptionalVector> getDirection();

    default MutableParticleEffect setDirection(Vector direction) {
        return this.setDirection(MutableOptionalVector.fromVector(direction));
    }

    MutableParticleEffect setDirection(OptionalVector vector);

    @Override
    Optional<ImmutableParticleData<?>> getData();

    default MutableParticleEffect setData(ParticleData<?> particleData) {
        return this.setData(Optional.ofNullable(particleData));
    }

    MutableParticleEffect setData(Optional<? extends ParticleData<?>> particleData);

    MutableParticleEffect setParticle(Particle particle);

    MutableParticleEffect modifyParticle(Function<Particle, Particle> modifier);

    MutableParticleEffect setAmount(int amount);

    MutableParticleEffect modifyAmount(Function<Integer, Integer> modifier);

    MutableParticleEffect modifyOffsets(Function<MutableOptionalVector, MutableOptionalVector> modifier);

    MutableParticleEffect removeOffsets();

    MutableParticleEffect modifyColor(Function<MutableParticleColor, MutableParticleColor> modifier);

    MutableParticleEffect removeColor();

    MutableParticleEffect setSpeed(Optional<Double> speed);

    default MutableParticleEffect setSpeed(double speed) {
        return this.setSpeed(Optional.of(speed));
    }

    MutableParticleEffect modifySpeed(Function<Double, Double> modifier);

    MutableParticleEffect removeSpeed();

    MutableParticleEffect modifyDirection(Function<MutableOptionalVector, MutableOptionalVector> modifier);

    MutableParticleEffect removeDirection();

    MutableParticleEffect modifyData(Function<MutableParticleData<?>, MutableParticleData<?>> modifier);

    MutableParticleEffect removeData();

    @Override
    default MutableParticleData<?> convertData(ParticleData<?> data) throws IllegalArgumentException {
        return ParticleEffect.super.convertData(data).toMutable();
    }

}