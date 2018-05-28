package io.prodity.commons.spigot.legacy.particle.effect.collect.list;

import io.prodity.commons.spigot.legacy.delegate.DelegateList;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import java.util.List;
import lombok.Getter;

public abstract class AbstractParticleEffectList<T extends ParticleEffect> implements ParticleEffectList<T>, DelegateList<T> {

    @Getter
    protected final List<T> delegateList;

    protected AbstractParticleEffectList() {
        this.delegateList = this.createList();
    }

    protected AbstractParticleEffectList(Iterable<T> iterable) {
        this.delegateList = this.createList(iterable);
    }

    protected AbstractParticleEffectList(T... elements) {
        this.delegateList = this.createList(elements);
    }

    protected abstract List<T> createList(Iterable<T> iterable);

    protected abstract List<T> createList(T... elements);

    protected abstract List<T> createList();

    @Override
    public List<T> getDelegateList() {
        return this.delegateList;
    }

}