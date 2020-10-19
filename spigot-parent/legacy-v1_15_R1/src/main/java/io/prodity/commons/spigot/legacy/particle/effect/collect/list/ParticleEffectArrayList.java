package io.prodity.commons.spigot.legacy.particle.effect.collect.list;

import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;

import java.util.List;

public class ParticleEffectArrayList<T extends ParticleEffect> extends AbstractParticleEffectList<T> {

    public static <T extends ParticleEffect> ParticleEffectArrayList<T> of() {
        return new ParticleEffectArrayList<>();
    }

    public static <T extends ParticleEffect> ParticleEffectArrayList<T> copyOf(Iterable<T> iterable) {
        return new ParticleEffectArrayList<>(iterable);
    }

    public static <T extends ParticleEffect> ParticleEffectArrayList<T> copyOf(T... elements) {
        return new ParticleEffectArrayList<>(elements);
    }

    protected ParticleEffectArrayList() {
        super();
    }

    protected ParticleEffectArrayList(Iterable<T> iterable) {
        super(iterable);
    }

    protected ParticleEffectArrayList(T... elements) {
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