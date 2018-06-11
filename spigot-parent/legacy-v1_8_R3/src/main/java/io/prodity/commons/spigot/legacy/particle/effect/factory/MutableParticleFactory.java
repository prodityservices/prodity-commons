package io.prodity.commons.spigot.legacy.particle.effect.factory;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.MutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.note.MutableParticleNoteColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ordinary.MutableOrdinaryParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.MutableParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.block.MutableBlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.MutableItemParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.impl.SimpleMutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.impl.SimpleMutableParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.offset.MutableOffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.MutableOptionalVector;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import org.bukkit.Material;

public class MutableParticleFactory implements
    ParticleFactory<MutableParticleEffect, MutableOffsetParticleEffect, MutableParticleData<?>, MutableParticleColor> {

    @Override
    public OptionalVector createEmptyVector() {
        return MutableOptionalVector.empty();
    }

    @Override
    public MutableOrdinaryParticleColor ordinaryColor(Color color) throws IllegalArgumentException {
        return new MutableOrdinaryParticleColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public MutableOrdinaryParticleColor ordinaryColor(int red, int green, int blue) throws IllegalArgumentException {
        return new MutableOrdinaryParticleColor(red, green, blue);
    }

    @Override
    public MutableParticleNoteColor noteColor(int note) throws IllegalArgumentException {
        return new MutableParticleNoteColor(note);
    }

    @Override
    public MutableItemParticleData itemData(Material material, byte data) {
        return new MutableItemParticleData(material, data);
    }

    @Override
    public MutableParticleEffect effect(ParticleEffect effect) {
        return new SimpleMutableParticleEffect(effect);
    }

    @Override
    public MutableBlockParticleData blockData(Material material, byte data) {
        return new MutableBlockParticleData(material, data);
    }

    @Override
    public MutableParticleEffect effect(Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed)
        throws IllegalArgumentException {
        return new SimpleMutableParticleEffect(particle, particleData, direction, speed);
    }

    @Override
    public MutableParticleEffect effect(Particle particle, int amount, OptionalVector offsets, Double speed, ParticleData<?> particleData)
        throws IllegalArgumentException {
        return new SimpleMutableParticleEffect(particle, amount, offsets, speed, particleData);
    }

    @Override
    public MutableParticleEffect effect(Particle particle, ParticleColor color) throws IllegalArgumentException {
        return new SimpleMutableParticleEffect(particle, color);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(ParticleEffect effect) {
        return new SimpleMutableOffsetParticleEffect(effect);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(ParticleEffect effect, OptionalVector locationOffset) {
        return new SimpleMutableOffsetParticleEffect(effect, locationOffset);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed,
        OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleMutableOffsetParticleEffect(particle, amount, offsets, speed, null, locationOffset);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed,
        OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleMutableOffsetParticleEffect(particle, particleData, direction, speed, locationOffset);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed,
        ParticleData<?> particleData, OptionalVector locationOffset) throws IllegalArgumentException {
        return new SimpleMutableOffsetParticleEffect(particle, amount, offsets, speed, particleData, locationOffset);
    }

    @Override
    public MutableOffsetParticleEffect offsetEffect(Particle particle, ParticleColor color, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return new SimpleMutableOffsetParticleEffect(particle, color, locationOffset);
    }

}