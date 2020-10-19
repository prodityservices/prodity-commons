package io.prodity.commons.spigot.legacy.particle.effect.color.note;

import io.prodity.commons.spigot.legacy.particle.effect.color.ImmutableParticleColor;

public class ImmutableParticleNoteColor implements ParticleNoteColor, ImmutableParticleColor {

    private final int note;

    public ImmutableParticleNoteColor(int note) throws IllegalArgumentException {
        ParticleNoteColor.verifyNote(note);
        this.note = note;
    }

    @Override
    public int getNote() {
        return this.note;
    }

    @Override
    public MutableParticleNoteColor toMutableParticleColor() {
        return new MutableParticleNoteColor(this.note);
    }

    @Override
    public ImmutableParticleNoteColor toImmutableParticleColor() {
        return this;
    }

}
