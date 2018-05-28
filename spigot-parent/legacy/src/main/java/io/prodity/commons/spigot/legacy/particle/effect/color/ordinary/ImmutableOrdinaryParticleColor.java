package io.prodity.commons.spigot.legacy.particle.effect.color.ordinary;

import io.prodity.commons.spigot.legacy.color.impl.SimpleImmutableColor;
import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;

public class ImmutableOrdinaryParticleColor extends SimpleImmutableColor implements OrdinaryParticleColor, ImmutableParticleColor {

    public ImmutableOrdinaryParticleColor(int red, int green, int blue) throws IllegalArgumentException {
        super(red, green, blue);
    }

    @Override
    public ImmutableOrdinaryParticleColor toImmutableParticleColor() {
        return this;
    }

    @Override
    public MutableOrdinaryParticleColor toMutableParticleColor() {
        return new MutableOrdinaryParticleColor(this.getRed(), this.getGreen(), this.getBlue());
    }

}