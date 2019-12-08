package io.prodity.commons.spigot.legacy.sound.playable.collect.list;

import io.prodity.commons.spigot.legacy.delegate.DelegateList;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import java.util.List;
import lombok.Getter;

public abstract class AbstractPlayableSoundList<T extends PlayableSound> implements PlayableSoundList<T>, DelegateList<T> {

    @Getter
    protected final List<T> delegateList;

    protected AbstractPlayableSoundList() {
        this.delegateList = this.createList();
    }

    protected AbstractPlayableSoundList(Iterable<T> iterable) {
        this.delegateList = this.createList(iterable);
    }

    protected AbstractPlayableSoundList(T... elements) {
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