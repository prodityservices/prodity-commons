package io.prodity.commons.spigot.legacy.particle.effect.factory;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.particle.effect.ImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.note.ImmutableParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ordinary.ImmutableOrdinaryParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ImmutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.block.ImmutableBlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.ImmutableItemParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.impl.SimpleImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.impl.SimpleImmutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.offset.ImmutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.ImmutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import org.bukkit.Material;
import org.bukkit.Particle;

public class ImmutableParticleFactory implements
    ParticleFactory<ImmutableParticleEffect, ImmutableOffsetParticleEffect, ImmutableParticleData<?>, ImmutableParticleColor> {

    @Override
    public OptionalVector createEmptyVector() {
        return ImmutableOptionalVector.empty();
    }

    @Override
    public ImmutableOrdinaryParticleColor ordinaryColor(Color color) throws IllegalArgumentException {
        return new ImmutableOrdinaryParticleColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public ImmutableOrdinaryParticleColor ordinaryColor(int red, int green, int blue) throws IllegalArgumentException {
        return new ImmutableOrdinaryParticleColor(red, green, blue);
    }

    @Override
    public ImmutableParticleNoteColor noteColor(int note) throws IllegalArgumentException {
        return new ImmutableParticleNoteColor(note);
    }

    @Override
    public ImmutableItemParticleData itemData(Material material, byte data) {
        return new ImmutableItemParticleData(material, data);
    }

    @Override
    public ImmutableBlockParticleData blockData(Material material, byte data) {
        return new ImmutableBlockParticleData(material, data);
    }

    @Override
    public ImmutableParticleEffect effect(ParticleEffect effect) {
        return new SimpleImmutableParticleEffect(effect);
    }

    @Override
    public ImmutableParticleEffect effect(Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed)
        throws IllegalArgumentException {
        return new SimpleImmutableParticleEffect(particle, particleData, direction, speed);
    }

    @Override
    public ImmutableParticleEffect effect(Particle particle, int amount, OptionalVector offsets, Double speed, ParticleData<?> particleData)
        throws IllegalArgumentException {
        return new SimpleImmutableParticleEffect(particle, amount, offsets, speed, particleData);
    }

    @Override
    public ImmutableParticleEffect effect(Particle particle, ParticleColor color) throws IllegalArgumentException {
        return new SimpleImmutableParticleEffect(particle, color);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(ParticleEffect effect) {
        return new SimpleImmutableOffsetParticleEffect(effect);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(ParticleEffect effect, OptionalVector locationOffset) {
        return new SimpleImmutableOffsetParticleEffect(effect, locationOffset);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed,
        OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleImmutableOffsetParticleEffect(particle, amount, offsets, speed, null, locationOffset);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(Particle particle, ParticleData<?> particleData, OptionalVector direction,
        Double speed,
        OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleImmutableOffsetParticleEffect(particle, particleData, direction, speed, locationOffset);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData, OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleImmutableOffsetParticleEffect(particle, amount, offsets, speed, particleData, locationOffset);
    }

    @Override
    public ImmutableOffsetParticleEffect offsetEffect(Particle particle, ParticleColor color, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return new SimpleImmutableOffsetParticleEffect(particle, color, locationOffset);
    }

}