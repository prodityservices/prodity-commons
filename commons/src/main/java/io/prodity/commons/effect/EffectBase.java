package io.prodity.commons.effect;

import com.google.common.base.Preconditions;

public abstract class EffectBase implements Effect {

    private final String id;

    public EffectBase(String id) {
        Preconditions.checkNotNull(id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

}