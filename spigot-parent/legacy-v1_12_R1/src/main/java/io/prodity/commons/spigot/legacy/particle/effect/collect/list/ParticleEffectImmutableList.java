package io.prodity.commons.spigot.legacy.particle.effect.collect.list;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.prodity.commons.spigot.legacy.particle.effect.ParticleEffect;
import java.util.Collection;
import java.util.List;

public class ParticleEffectImmutableList<T extends ParticleEffect> extends AbstractParticleEffectList<T> {

    public static class Builder<T extends ParticleEffect> {

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

        public ParticleEffectImmutableList<T> build() {
            return new ParticleEffectImmutableList<>(this.elements);
        }

    }

    public static <T extends ParticleEffect> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T extends ParticleEffect> ParticleEffectImmutableList<T> of() {
        return new ParticleEffectImmutableList<>();
    }

    public static <T extends ParticleEffect> ParticleEffectImmutableList<T> copyOf(Iterable<T> iterable) {
        return new ParticleEffectImmutableList<>(iterable);
    }

    public static <T extends ParticleEffect> ParticleEffectImmutableList<T> copyOf(T... elements) {
        return new ParticleEffectImmutableList<>(elements);
    }

    protected ParticleEffectImmutableList() {
        super();
    }

    protected ParticleEffectImmutableList(Iterable<T> iterable) {
        super(iterable);
    }

    protected ParticleEffectImmutableList(T... elements) {
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