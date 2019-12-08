package io.prodity.commons.spigot.legacy.particle.effect.color;

import org.bukkit.util.Vector;

public interface ParticleColor {

    double getXOffset();

    double getYOffset();

    double getZOffset();

    default Vector toVector() {
        return new Vector(this.getXOffset(), this.getYOffset(), this.getZOffset());
    }

    ImmutableParticleColor toImmutableParticleColor();

    MutableParticleColor toMutableParticleColor();

}