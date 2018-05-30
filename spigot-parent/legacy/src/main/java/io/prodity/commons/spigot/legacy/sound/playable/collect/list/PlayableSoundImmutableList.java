package io.prodity.commons.spigot.legacy.sound.playable.collect.list;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import java.util.Collection;
import java.util.List;

public class PlayableSoundImmutableList<T extends PlayableSound> extends AbstractPlayableSoundList<T> {

    public static class Builder<T extends PlayableSound> {

        private final List<T> elements;

        private Builder() {
            this.elements = Lists.newArrayList();
        }

        public Builder<T> add(T element) {
            this.elements.add(element);
            return this;
        }

        public Builder<T> addAll(T... elements) {
            for (T element : elements) {
                this.elements.add(element);
            }
            return this;
        }

        public Builder<T> addAll(Collection<? extends T> iterable) {
            this.elements.addAll(iterable);
            return this;
        }

        public PlayableSoundImmutableList<T> build() {
            return new PlayableSoundImmutableList<>(this.elements);
        }

    }

    public static <T extends PlayableSound> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T extends PlayableSound> PlayableSoundImmutableList<T> of() {
        return new PlayableSoundImmutableList<>();
    }

    public static <T extends PlayableSound> PlayableSoundImmutableList<T> copyOf(Iterable<T> iterable) {
        return new PlayableSoundImmutableList<>(iterable);
    }

    public static <T extends PlayableSound> PlayableSoundImmutableList<T> copyOf(T... elements) {
        return new PlayableSoundImmutableList<>(elements);
    }

    protected PlayableSoundImmutableList() {
        super();
    }

    protected PlayableSoundImmutableList(Iterable<T> iterable) {
        super(iterable);
    }

    protected PlayableSoundImmutableList(T... elements) {
        super(elements);
    }

    @Override
    protected List<T> createList(Iterable<T> iterable) {
        return ImmutableList.copyOf(iterable);
    }

    @Override
    protected List<T> createList(T[] elements) {
        return ImmutableList.copyOf(elements);
    }

    @Override
    protected List<T> createList() {
        return ImmutableList.of();
    }

}