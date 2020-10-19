package io.prodity.commons.spigot.legacy.particle.effect.impl;

import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.offset.ImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.offset.OffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Particle;

public class SimpleImmutableOffsetParticleEffect extends SimpleImmutableParticleEffect implements ImmutableOffsetParticleEffect {

    @Getter
    private final ImmutableOptionalVector locationOffset;

    public SimpleImmutableOffsetParticleEffect(@NonNull ParticleEffect effect) {
        super(effect);
        if (effect instanceof OffsetParticleEffect) {
            final OffsetParticleEffect offsetEffect = (OffsetParticleEffect) effect;
            this.locationOffset = offsetEffect.getLocationOffset().toImmutable();
        } else {
            this.locationOffset = ImmutableOptionalVector.empty();
        }
    }

    public SimpleImmutableOffsetParticleEffect(@NonNull ParticleEffect effect, OptionalVector locationOffset) {
        super(effect);
        this.locationOffset = locationOffset != null ? locationOffset.toImmutable() : ImmutableOptionalVector.empty();
    }

    public SimpleImmutableOffsetParticleEffect(@NonNull Particle particle, ParticleData<?> particleData, OptionalVector direction,
        Double speed, OptionalVector locationOffset) {
        super(particle, particleData, direction, speed);
        this.locationOffset = locationOffset != null ? locationOffset.toImmutable() : ImmutableOptionalVector.empty();
    }

    public SimpleImmutableOffsetParticleEffect(@NonNull Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData, OptionalVector locationOffset) throws IllegalArgumentException {
        super(particle, amount, offsets, speed, particleData);
        this.locationOffset = locationOffset != null ? locationOffset.toImmutable() : ImmutableOptionalVector.empty();
    }

    public SimpleImmutableOffsetParticleEffect(@NonNull Particle particle, @NonNull ParticleColor color, OptionalVector locationOffset)
        throws IllegalArgumentException {
        super(particle, color);
        this.locationOffset = locationOffset != null ? locationOffset.toImmutable() : ImmutableOptionalVector.empty();
    }

    @Override
    protected Location getLocationToPlayAt(Location location) {
        return location.clone().add(this.locationOffset.getBukkitVector());
    }

}