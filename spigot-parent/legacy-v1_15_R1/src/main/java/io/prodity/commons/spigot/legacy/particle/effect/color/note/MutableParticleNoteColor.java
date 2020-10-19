package io.prodity.commons.spigot.legacy.particle.effect.color.note;

import io.prodity.commons.spigot.legacy.particle.effect.color.MutableParticleColor;
import lombok.Getter;

public class MutableParticleNoteColor implements ParticleNoteColor, MutableParticleColor {

    @Getter
    private int note;

    public MutableParticleNoteColor(int note) throws IllegalArgumentException {
        ParticleNoteColor.verifyNote(note);
        this.note = note;
    }

    public void setNote(int note) {
        ParticleNoteColor.verifyNote(note);
        this.note = note;
    }

    @Override
    public MutableParticleNoteColor toMutableParticleColor() {
        return new MutableParticleNoteColor(this.note);
    }

    @Override
    public ImmutableParticleNoteColor toImmutableParticleColor() {
        return new ImmutableParticleNoteColor(this.note);
    }

}