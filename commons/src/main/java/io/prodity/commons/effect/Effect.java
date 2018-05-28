package io.prodity.commons.effect;

import io.prodity.commons.identity.Identifiable;

public interface Effect extends Identifiable<String> {

    void play(EffectOption... options);

}