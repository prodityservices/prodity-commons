package io.prodity.commons.spigot.legacy.particle.effect.color.ordinary;

import io.prodity.commons.spigot.legacy.color.Color;
import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;

public interface OrdinaryParticleColor extends ParticleColor, Color {

    @Override
    default double getXOffset() {
        final double value = this.getRed() / 255D;
        return value > 0D ? value : Float.MIN_VALUE;
    }

    @Override
    default double getYOffset() {
        return this.getGreen() / 255D;
    }

    @Override
    default double getZOffset() {
        return this.getBlue() / 255D;
    }

}