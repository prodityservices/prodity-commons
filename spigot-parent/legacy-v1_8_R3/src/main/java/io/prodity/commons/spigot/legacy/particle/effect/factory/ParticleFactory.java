package io.prodity.commons.spigot.legacy.particle.effect.factory;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.particle.Particle;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;
import io.prodity.commons.spigot.legacy.particle.effect.data.ParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.offset.OffsetParticleEffect;
import io.prodity.commons.spigot.legacy.vector.optional.OptionalVector;
import org.bukkit.Material;

public interface ParticleFactory<T1 extends ParticleEffect, T2 extends OffsetParticleEffect, T3 extends ParticleData<?>, T4 extends ParticleColor> {

    OptionalVector createEmptyVector();

    T4 ordinaryColor(int red, int green, int blue) throws IllegalArgumentException;

    T4 ordinaryColor(Color color) throws IllegalArgumentException;

    T4 noteColor(int note) throws IllegalArgumentException;

    T3 itemData(Material material, byte data);

    default T3 itemData(Material material) {
        return this.itemData(material, (byte) 0);
    }

    T3 blockData(Material material, byte data);

    default T3 blockData(Material material) {
        return this.blockData(material, (byte) 0);
    }

    T1 effect(ParticleEffect effect);

    T1 effect(Particle particle, int amount, OptionalVector offsets, Double speed, ParticleData<?> particleData)
        throws IllegalArgumentException;

    default T1 effect(Particle particle, int amount, OptionalVector offsets, ParticleData<?> particleData) throws IllegalArgumentException {
        return this.effect(particle, amount, offsets, null, particleData);
    }

    default T1 effect(Particle particle, int amount, ParticleData<?> particleData) throws IllegalArgumentException {
        return this.effect(particle, amount, null, particleData);
    }

    default T1 effect(Particle particle, int amount, OptionalVector offsets) throws IllegalArgumentException {
        return this.effect(particle, amount, offsets, null, null);
    }

    default T1 effect(Particle particle, int amount, Double speed) throws IllegalArgumentException {
        return this.effect(particle, amount, null, speed, null);
    }

    default T1 effect(Particle particle, int amount, Double speed, OptionalVector offsets) throws IllegalArgumentException {
        return this.effect(particle, amount, offsets, speed, null);
    }

    default T1 effect(Particle particle, int amount) throws IllegalArgumentException {
        return this.effect(particle, amount, null, null, null);
    }

    T1 effect(Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed) throws IllegalArgumentException;

    default T1 effect(Particle particle, ParticleData<?> particleData, OptionalVector direction) throws IllegalArgumentException {
        return this.effect(particle, particleData, direction, null);
    }

    default T1 effect(Particle particle, ParticleData<?> particleData, Double speed) throws IllegalArgumentException {
        return this.effect(particle, particleData, null, speed);
    }

    default T1 effect(Particle particle, ParticleData<?> particleData) throws IllegalArgumentException {
        return this.effect(particle, particleData, null, null);
    }

    default T1 effect(Particle particle, OptionalVector direction, Double speed) throws IllegalArgumentException {
        return this.effect(particle, null, direction, speed);
    }

    T1 effect(Particle particle, ParticleColor color) throws IllegalArgumentException;

    T2 offsetEffect(ParticleEffect effect);

    T2 offsetEffect(ParticleEffect effect, OptionalVector locationOffset);

    T2 offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed, OptionalVector locationOffset)
        throws IllegalArgumentException;

    default T2 offsetEffect(Particle particle, int amount, OptionalVector offsets, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, amount, offsets, (Double) null, locationOffset);
    }

    default T2 offsetEffect(Particle particle, int amount, Double speed, OptionalVector locationOffset) throws IllegalArgumentException {
        return this.offsetEffect(particle, amount, null, speed, locationOffset);
    }

    default T2 offsetEffect(Particle particle, int amount, OptionalVector locationOffset) throws IllegalArgumentException {
        return this.offsetEffect(particle, amount, null, (Double) null, locationOffset);
    }

    T2 offsetEffect(Particle particle, ParticleData<?> particleData, OptionalVector direction, Double speed, OptionalVector locationOffset)
        throws IllegalArgumentException;

    default T2 offsetEffect(Particle particle, ParticleData<?> particleData, OptionalVector direction, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, particleData, direction, null, locationOffset);
    }

    default T2 offsetEffect(Particle particle, ParticleData<?> particleData, Double speed, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, particleData, null, speed, locationOffset);
    }

    default T2 offsetEffect(Particle particle, ParticleData<?> particleData, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, particleData, null, null, locationOffset);
    }

    default T2 offsetEffect(Particle particle, OptionalVector direction, Double speed, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, null, direction, speed, locationOffset);
    }

    T2 offsetEffect(Particle particle, int amount, OptionalVector offsets, Double speed, ParticleData<?> particleData,
        OptionalVector locationOffset)
        throws IllegalArgumentException;

    default T2 offsetEffect(Particle particle, int amount, OptionalVector offsets, ParticleData<?> particleData,
        OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, amount, offsets, null, particleData, locationOffset);
    }

    default T2 offsetEffect(Particle particle, int amount, ParticleData<?> particleData, OptionalVector locationOffset)
        throws IllegalArgumentException {
        return this.offsetEffect(particle, amount, null, particleData, locationOffset);
    }

    T2 offsetEffect(Particle particle, ParticleColor color, OptionalVector locationOffset) throws IllegalArgumentException;

}