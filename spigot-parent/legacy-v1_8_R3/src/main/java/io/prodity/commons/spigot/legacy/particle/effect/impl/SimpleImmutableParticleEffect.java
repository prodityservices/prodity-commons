package io.prodity.commons.spigot.legacy.particle.effect.impl;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.AbstractParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.spawn.ParticleSpawnStrategy;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

public class SimpleImmutableParticleEffect extends AbstractParticleEffect implements ImmutableParticleEffect {

    @Getter
    private final Particle particle;

    @Getter
    private final int amount;

    @Getter
    private final ImmutableOptionalVector offsets;

    @Getter
    private final Optional<ImmutableParticleColor> color;

    @Getter
    private final Optional<ImmutableOptionalVector> direction;

    @Getter
    private final Optional<Double> speed;

    @Getter
    private final Optional<ImmutableParticleData<?>> data;

    private final LazyValue<ParticleSpawnStrategy> spawnStrategy = new SimpleLazyValue<>(this.getSpawnStrategyGenerator()::generate);

    public SimpleImmutableParticleEffect(@NonNull ParticleEffect effect) {
        this.particle = effect.getParticle();
        this.amount = effect.getAmount();
        this.speed = effect.getSpeed();
        this.offsets = effect.getOffsets().toImmutable();
        this.color = effect.getColor().map(ParticleColor::toImmutableParticleColor);
        this.data = effect.getData().map(this::convertData);
        this.direction = effect.getDirection().map(OptionalVector::toImmutable);
    }

    public SimpleImmutableParticleEffect(@NonNull Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed) {
        this.particle = particle;
        this.data = Optional.ofNullable(particleData).map(this::convertData);

        if (this.data.isPresent()) {
            ParticleEffects.verifyDataRequired(particle);
        } else {
            ParticleEffects.verifyNoDataRequired(particle);
        }

        this.direction = Optional.ofNullable(direction).map(OptionalVector::toImmutable);
        if (this.direction.isPresent()) {
            ParticleEffects.verifyDirectional(particle);
            this.amount = 0;
        } else {
            this.amount = 1;
        }

        this.offsets = this.direction.map(OptionalVector::toImmutable).get();
        this.speed = Optional.of(speed);
        this.color = Optional.empty();
    }

    public SimpleImmutableParticleEffect(@NonNull Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData) throws IllegalArgumentException {
        this.particle = particle;
        this.data = Optional.ofNullable(particleData).map(this::convertData);

        if (this.data.isPresent()) {
            ParticleEffects.verifyDataRequired(particle);
        } else {
            ParticleEffects.verifyNoDataRequired(particle);
        }

        this.amount = amount;
        this.offsets = offsets != null ? offsets.toImmutable() : ImmutableOptionalVector.empty();
        this.speed = Optional.ofNullable(speed);

        this.direction = Optional.empty();
        this.color = Optional.empty();
    }

    public SimpleImmutableParticleEffect(@NonNull Particle particle, @NonNull ParticleColor color) throws IllegalArgumentException {
        ParticleEffects.verifyColor(particle, color);
        ParticleEffects.verifyNoDataRequired(particle);
        this.particle = particle;

        this.color = Optional.of(color).map(ParticleColor::toImmutableParticleColor);
        this.offsets = ImmutableOptionalVector.fromVector(color.toVector());
        this.direction = Optional.empty();

        this.amount = 0;

        this.speed = Optional.of(1D);
        this.data = Optional.empty();
    }

    @Override
    public ParticleSpawnStrategy getSpawnStrategy() {
        return this.spawnStrategy.get();
    }

}