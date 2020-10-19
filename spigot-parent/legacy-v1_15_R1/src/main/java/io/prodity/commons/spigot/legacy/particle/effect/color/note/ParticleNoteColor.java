package io.prodity.commons.spigot.legacy.particle.effect.color.note;

import io.prodity.commons.spigot.legacy.particle.effect.color.ParticleColor;

public interface ParticleNoteColor extends ParticleColor {

    static void verifyNote(int note) throws IllegalArgumentException {
        if (note < 0 || note > 24) {
            throw new IllegalArgumentException("note value '" + note + "' must fit the range 0-24 (inclusive)");
        }
    }

    int getNote();

    @Override
    default double getXOffset() {
        return (double) this.getNote() / 24D;
    }

    @Override
    default double getYOffset() {
        return 0D;
    }

    @Override
    default double getZOffset() {
        return 0D;
    }

}