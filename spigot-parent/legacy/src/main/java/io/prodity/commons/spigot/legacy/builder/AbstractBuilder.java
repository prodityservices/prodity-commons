package io.prodity.commons.spigot.legacy.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.ImmutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.MutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractBuilder<T, C extends BuilderConstruction<T>, SELF extends Builder<T, C, SELF>> implements
    Builder<T, C, SELF> {

    @Getter
    private List<Function<T, T>> postConstructHandlers;

    protected AbstractBuilder() {
        this.postConstructHandlers = Lists.newLinkedList();
    }

    protected AbstractBuilder(Builder<T, C, ?> builder) {
        this.setPostConstructHandlers(builder.getPostConstructHandlers());
    }

    @Override
    public SELF setPostConstructHandlers(@NonNull List<Function<T, T>> postConstructHandlers) {
        this.postConstructHandlers = Lists.newArrayList(postConstructHandlers);
        return this.getSelf();
    }

    @Override
    public boolean containsMeta(BuilderMeta<?, C> meta) {
        return this.getMetaList().contains(meta);
    }

    @Override
    public boolean containsMetaKey(BuilderMetaKey<?, C, ?, ?> key) {
        return this.getMetaMap().containsKey(key);
    }

    @Override
    public <M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>, V> V getMetaValue(
        @NonNull BuilderMetaKey<V, C, M, I> key) {
        return (V) this.getMetaMap().get(key);
    }

    @Override
    public SELF addPostConstructHandler(@NonNull Function<T, T> function) {
        this.postConstructHandlers.add(function);
        return this.getSelf();
    }

    protected <V> V getModifiedValue(V value, Collection<MetaModifier<?>> modifiers) {
        if (modifiers.isEmpty()) {
            return value;
        }
        for (MetaModifier<?> modifier : modifiers) {
            value = ((MetaModifier<V>) modifier).apply(value);
        }
        return value;
    }

    protected <V> V getValueToApply(BuilderMeta<V, C> meta, Collection<MetaModifier<?>> additionalModifiers) {
        final BuilderMetaKey<V, C, ?, ?> key = meta.getKey();
        final Collection<MetaModifier<?>> modifiers = this.getMetaModifiers().get(key);
        if (modifiers.isEmpty() && additionalModifiers.isEmpty()) {
            return meta.getValue();
        }
        V value = meta.getValue();
        value = this.getModifiedValue(value, modifiers);
        value = this.getModifiedValue(value, additionalModifiers);
        return value;
    }

    protected <V> V getValueToApply(BuilderMeta<V, C> meta) {
        final BuilderMetaKey<V, C, ?, ?> key = meta.getKey();
        final Collection<MetaModifier<?>> modifiers = this.getMetaModifiers().get(key);
        if (modifiers.isEmpty()) {
            return meta.getValue();
        }
        return this.getModifiedValue(meta.getValue(), modifiers);
    }

    protected T construct(Function<SELF, C> constructionFunction,
        Multimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> additionalModifiers) {
        final C construction = constructionFunction.apply(this.getSelf());
        return this.construct(() -> construction, this.getMetaList(), (meta) -> {
            final Collection<MetaModifier<?>> modifiers = additionalModifiers.get(meta.getKey());
            return this.getValueToApply(meta, modifiers);
        });
    }

    protected T construct(Function<SELF, C> constructionSupplier) {
        return this.construct(() -> constructionSupplier.apply(this.getSelf()), this.getMetaList(), this::getValueToApply);
    }

    protected T construct(Function<SELF, C> constructionSupplier, Collection<? extends BuilderMeta<?, C>> metaCollection) {
        return this.construct(() -> constructionSupplier.apply(this.getSelf()), metaCollection, this::getValueToApply);
    }

    protected T construct(Supplier<C> constructionSupplier) {
        return this.construct(constructionSupplier, this.getMetaList(), this::getValueToApply);
    }

    protected T construct(Supplier<C> constructionSupplier, Collection<? extends BuilderMeta<?, C>> metaCollection,
        Function<BuilderMeta<?, C>, ?> valueResolver) {
        final C construction = constructionSupplier.get();

        metaCollection.forEach(meta -> this.applyMetaToConstruction(construction, meta, valueResolver));

        T product = construction.construct();
        for (Function<T, T> handler : this.postConstructHandlers) {
            product = handler.apply(product);
        }

        return product;
    }

    private <V> void applyMetaToConstruction(C construction, BuilderMeta<V, C> meta, Function<BuilderMeta<?, C>, ?> valueResolver) {
        final V value = (V) valueResolver.apply(meta);
        meta.apply(construction, value);
    }

}