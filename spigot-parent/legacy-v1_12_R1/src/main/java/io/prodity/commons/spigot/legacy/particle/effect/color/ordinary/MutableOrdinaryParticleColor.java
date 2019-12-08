package io.prodity.commons.spigot.legacy.particle.effect.color.ordinary;

import io.prodity.commons.spigot.legacy.color.impl.SimpleMutableColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;

public class MutableOrdinaryParticleColor extends SimpleMutableColor implements OrdinaryParticleColor, MutableParticleColor {

    public MutableOrdinaryParticleColor(int red, int green, int blue) throws IllegalArgumentException {
        super(red, green, blue);
    }

    @Override
    public MutableOrdinaryParticleColor toMutableParticleColor() {
        return new MutableOrdinaryParticleColor(this.getRed(), this.getGreen(), this.getBlue());
    }

    @Override
    public ImmutableOrdinaryParticleColor toImmutableParticleColor() {
        return new ImmutableOrdinaryParticleColor(this.getRed(), this.getGreen(), this.getBlue());
    }

}