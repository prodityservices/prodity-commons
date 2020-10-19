package io.prodity.commons.spigot.legacy.particle.effect.impl;

import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.offset.MutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.offset.OffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.function.Function;

public class SimpleMutableOffsetParticleEffect extends SimpleMutableParticleEffect implements MutableOffsetParticleEffect {

    @Getter
    @NonNull
    private MutableOptionalVector locationOffset;

    public SimpleMutableOffsetParticleEffect(@NonNull ParticleEffect effect) {
        super(effect);
        if (effect instanceof OffsetParticleEffect) {
            final OffsetParticleEffect offsetEffect = (OffsetParticleEffect) effect;
            this.locationOffset = offsetEffect.getLocationOffset().toMutable();
        } else {
            this.locationOffset = MutableOptionalVector.empty();
        }
    }

    public SimpleMutableOffsetParticleEffect(@NonNull ParticleEffect effect, OptionalVector locationOffset) {
        super(effect);
        this.locationOffset = locationOffset != null ? locationOffset.toMutable() : MutableOptionalVector.empty();
    }

    public SimpleMutableOffsetParticleEffect(@NonNull Particle particle, ParticleData<?> particleData, OptionalVector direction,
        Double speed, OptionalVector locationOffset) {
        super(particle, particleData, direction, speed);
        this.locationOffset = locationOffset != null ? locationOffset.toMutable() : MutableOptionalVector.empty();
    }

    public SimpleMutableOffsetParticleEffect(@NonNull Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData, OptionalVector locationOffset) throws IllegalArgumentException {
        super(particle, amount, offsets, speed, particleData);
        this.locationOffset = locationOffset != null ? locationOffset.toMutable() : MutableOptionalVector.empty();
    }

    public SimpleMutableOffsetParticleEffect(@NonNull Particle particle, @NonNull ParticleColor color, OptionalVector locationOffset)
        throws IllegalArgumentException {
        super(particle, color);
        this.locationOffset = locationOffset != null ? locationOffset.toMutable() : MutableOptionalVector.empty();
    }

    @Override
    public SimpleMutableOffsetParticleEffect setLocationOffset(MutableOptionalVector locationOffset) {
        this.locationOffset = locationOffset == null ? MutableOptionalVector.empty() : locationOffset;
        return this;
    }

    @Override
    public SimpleMutableOffsetParticleEffect modifyLocationOffset(Function<MutableOptionalVector, MutableOptionalVector> modifier) {
        final MutableOptionalVector vector = modifier.apply(this.locationOffset);
        return this.setLocationOffset(vector);
    }

    @Override
    protected Location getLocationToPlayAt(Location location) {
        return location.clone().add(this.locationOffset.getBukkitVector());
    }

}