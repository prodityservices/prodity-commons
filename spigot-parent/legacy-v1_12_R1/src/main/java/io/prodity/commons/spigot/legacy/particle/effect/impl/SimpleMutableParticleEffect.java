package io.prodity.commons.spigot.legacy.particle.effect.impl;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.particle.effect.AbstractParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.MutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffects;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.spawn.ParticleSpawnStrategy;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Particle;

public class SimpleMutableParticleEffect extends AbstractParticleEffect implements MutableParticleEffect {

    private final LazyValue<ParticleSpawnStrategy> spawnStrategy = new SimpleLazyValue<>(this.getSpawnStrategyGenerator()::generate);

    @Getter
    private Particle particle;
    @Getter
    private int amount;
    private MutableOptionalVector offsets;
    private Optional<MutableParticleColor> color;
    private Optional<MutableOptionalVector> direction;
    @Getter
    private Optional<Double> speed;
    private Optional<MutableParticleData<?>> data;

    public SimpleMutableParticleEffect(@NonNull ParticleEffect effect) {
        this.particle = effect.getParticle();
        this.amount = effect.getAmount();
        this.speed = effect.getSpeed();
        this.offsets = effect.getOffsets().toMutable();
        this.color = effect.getColor().map(ParticleColor::toMutableParticleColor);
        this.data = effect.getData().map(this::convertData);
        this.direction = effect.getDirection().map(OptionalVector::toMutable);
    }

    public SimpleMutableParticleEffect(@NonNull Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed) {
        this.particle = particle;
        this.data = Optional.ofNullable(particleData).map(this::convertData);

        if (this.data.isPresent()) {
            ParticleEffects.verifyDataRequired(particle);
        } else {
            ParticleEffects.verifyNoDataRequired(particle);
        }

        this.direction = Optional.ofNullable(direction).map(OptionalVector::toMutable);
        if (this.direction.isPresent()) {
            ParticleEffects.verifyDirectional(particle);
            this.amount = 0;
        } else {
            this.amount = 1;
        }

        this.offsets = this.direction.map(OptionalVector::toMutable).get();
        this.speed = Optional.of(speed);
        this.color = Optional.empty();
    }

    public SimpleMutableParticleEffect(@NonNull Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData) throws IllegalArgumentException {
        this.particle = particle;
        this.data = Optional.ofNullable(particleData).map(this::convertData);

        if (this.data.isPresent()) {
            ParticleEffects.verifyDataRequired(particle);
        } else {
            ParticleEffects.verifyNoDataRequired(particle);
        }

        this.amount = amount;
        this.offsets = offsets != null ? offsets.toMutable() : MutableOptionalVector.empty();
        this.speed = Optional.ofNullable(speed);

        this.direction = Optional.empty();
        this.color = Optional.empty();
    }

    public SimpleMutableParticleEffect(@NonNull Particle particle, @NonNull ParticleColor color) throws IllegalArgumentException {
        ParticleEffects.verifyColor(particle, color);
        ParticleEffects.verifyNoDataRequired(particle);
        this.particle = particle;

        this.color = Optional.of(color).map(ParticleColor::toMutableParticleColor);
        this.offsets = MutableOptionalVector.fromVector(color.toVector());
        this.direction = Optional.empty();

        this.amount = 0;

        this.speed = Optional.of(1D);
        this.data = Optional.empty();
    }

    public SimpleMutableParticleEffect updateSpawnStrategy() {
        this.spawnStrategy.update();
        return this;
    }

    @Override
    public ImmutableOptionalVector getOffsets() {
        return this.offsets.toImmutable();
    }

    @Override
    public SimpleMutableParticleEffect setOffsets(OptionalVector vector) {
        if (this.direction.isPresent()) {
            this.direction = Optional.empty();
        }
        if (this.color.isPresent()) {
            this.color = Optional.empty();
        }
        this.offsets = vector.toMutable();
        return this.updateSpawnStrategy();
    }

    @Override
    public Optional<ImmutableParticleColor> getColor() {
        return this.color.map(ParticleColor::toImmutableParticleColor);
    }

    @Override
    public SimpleMutableParticleEffect setColor(Optional<ParticleColor> color) throws IllegalArgumentException {
        if (Objects.equals(this.color.orElse(null), color.orElse(null))) {
            return this;
        }

        if (!color.isPresent()) {
            return this.removeColor();
        }

        ParticleEffects.verifyColor(this.particle, color.get());
        this.color = Optional.of(color.get().toMutableParticleColor());
        this.offsets = MutableOptionalVector.fromVector(color.get().toVector());
        this.direction = Optional.empty();
        this.amount = 0;
        this.speed = Optional.of(1D);
        this.data = Optional.empty();

        return this.updateSpawnStrategy();
    }

    @Override
    public Optional<ImmutableOptionalVector> getDirection() {
        return this.direction.map(OptionalVector::toImmutable);
    }

    @Override
    public SimpleMutableParticleEffect setDirection(OptionalVector vector) {
        if (vector == null || !vector.isAnyPresent()) {
            return this.removeDirection();
        }
        ParticleEffects.verifyDirectional(this.particle);
        if (this.color.isPresent()) {
            this.color = Optional.empty();
        }
        this.amount = 0;
        this.direction = Optional.ofNullable(vector.toMutable());
        this.offsets = vector.toMutable();
        return this.updateSpawnStrategy();
    }

    @Override
    public Optional<ImmutableParticleData<?>> getData() {
        return this.data.map(ParticleData::toImmutable);
    }

    @Override
    public SimpleMutableParticleEffect setData(Optional<? extends ParticleData<?>> data) {
        if (!data.isPresent()) {
            ParticleEffects.verifyNoDataRequired(this.particle);
            return this.removeData();
        } else {
            ParticleEffects.verifyData(this.particle, data.get());
        }

        this.data = data.map(ParticleData::toMutable);
        this.color = Optional.empty();

        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect modifyParticle(Function<Particle, Particle> modifier) {
        final Particle particle = modifier.apply(this.particle);
        return this.setParticle(particle);
    }

    @Override
    public SimpleMutableParticleEffect setParticle(@NonNull Particle particle) {
        if (this.color.isPresent() && !ParticleEffects.isValidColor(particle, this.color.get())) {
            this.removeColor();
        }
        if (this.direction.isPresent() && !ParticleEffects.isDirectional(particle)) {
            this.removeDirection();
        }
        if (this.data.isPresent() && !ParticleEffects.isValidData(particle, this.data.get())) {
            this.removeData();
        }
        this.particle = particle;
        return this.updateSpawnStrategy();
    }

    @Override
    public MutableParticleEffect modifyAmount(Function<Integer, Integer> modifier) {
        final int amount = modifier.apply(this.amount);
        return this.setAmount(amount);
    }

    @Override
    public SimpleMutableParticleEffect setAmount(int amount) {
        ParticleEffects.verifyAmount(amount);
        this.amount = amount;
        return this.updateSpawnStrategy();
    }

    @Override
    public MutableParticleEffect modifyData(Function<MutableParticleData<?>, MutableParticleData<?>> modifier) {
        final MutableParticleData<?> data = modifier.apply(this.data.orElse(null));
        return this.setData(data);
    }

    @Override
    public SimpleMutableParticleEffect removeData() {
        if (!this.data.isPresent()) {
            return this;
        }
        ParticleEffects.verifyNoDataRequired(this.particle);
        this.data = Optional.empty();
        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect modifyColor(Function<MutableParticleColor, MutableParticleColor> modifier) {
        final MutableParticleColor color = modifier.apply(this.color.orElse(null));
        return this.setColor(Optional.ofNullable(color));
    }

    @Override
    public SimpleMutableParticleEffect removeColor() {
        if (!this.color.isPresent()) {
            return this;
        }
        this.color = Optional.empty();
        this.offsets = MutableOptionalVector.empty();
        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect modifySpeed(Function<Double, Double> modifier) {
        final Double speed = modifier.apply(this.speed.orElse(null));
        return this.setSpeed(Optional.ofNullable(speed));
    }

    @Override
    public SimpleMutableParticleEffect setSpeed(Optional<Double> speed) {
        this.speed = speed;
        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect removeSpeed() {
        if (!this.speed.isPresent()) {
            return this;
        }
        this.speed = Optional.empty();
        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect modifyDirection(Function<MutableOptionalVector, MutableOptionalVector> modifier) {
        final MutableOptionalVector vector = modifier.apply(this.direction.orElse(null));
        return this.setDirection(vector);
    }

    @Override
    public SimpleMutableParticleEffect removeDirection() {
        if (!this.direction.isPresent()) {
            return this;
        }
        this.direction = Optional.empty();
        this.offsets = MutableOptionalVector.empty();
        return this.updateSpawnStrategy();
    }

    @Override
    public SimpleMutableParticleEffect modifyOffsets(Function<MutableOptionalVector, MutableOptionalVector> modifier) {
        final MutableOptionalVector offsets = modifier.apply(this.offsets);
        return this.setOffsets(offsets);
    }

    @Override
    public SimpleMutableParticleEffect removeOffsets() {
        return this.setOffsets(MutableOptionalVector.empty());
    }

    @Override
    protected ParticleSpawnStrategy getSpawnStrategy() {
        return this.spawnStrategy.get();
    }

}