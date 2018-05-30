package io.prodity.commons.spigot.legacy.sound.playable.collect.list;

import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import java.util.List;

public class PlayableSoundArrayList<T extends PlayableSound> extends AbstractPlayableSoundList<T> {

    public static <T extends PlayableSound> PlayableSoundArrayList<T> of() {
        return new PlayableSoundArrayList<>();
    }

    public static <T extends PlayableSound> PlayableSoundArrayList<T> copyOf(Iterable<T> iterable) {
        return new PlayableSoundArrayList<>(iterable);
    }

    public static <T extends PlayableSound> PlayableSoundArrayList<T> copyOf(T... elements) {
        return new PlayableSoundArrayList<>(elements);
    }

    protected PlayableSoundArrayList() {
        super();
    }

    protected PlayableSoundArrayList(Iterable<T> iterable) {
        super(iterable);
    }

    protected PlayableSoundArrayList(T... elements) {
        super(elements);
    }

    @Override
    protected List<T> createList(Iterable<T> iterable) {
        return Lists.newArrayList(iterable);
    }

    @Override
    protected List<T> createList(T[] elements) {
        return Lists.newArrayList(elements);
    }

    @Override
    protected List<T> createList() {
        return Lists.newArrayList();
    }

}