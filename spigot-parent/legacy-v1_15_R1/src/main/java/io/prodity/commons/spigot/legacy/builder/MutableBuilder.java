package io.prodity.commons.spigot.legacy.builder;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.ImmutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.MutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import io.prodity.commons.spigot.legacy.builder.meta.resolve.MetaResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class MutableBuilder<T, C extends BuilderConstruction<T>, SELF extends MutableBuilder<T, C, SELF>> extends
    AbstractBuilder<T, C, SELF> {

    @Getter
    @Setter
    private final Multimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> metaModifiers;
    @Getter
    @Accessors(chain = true)
    private Map<BuilderMetaKey<?, C, ?, ?>, MutableBuilderMeta<?, C>> metaMap;

    protected MutableBuilder(Builder<T, C, ?> builder) {
        super(builder);
        this.metaMap = Maps.newHashMap();
        this.metaModifiers = MultimapBuilder.hashKeys().arrayListValues().build();
        this.metaModifiers.putAll(builder.getMetaModifiers());
        builder.getMetaMap().forEach((key, meta) -> this.metaMap.put(key, meta.toMutable()));
    }

    protected MutableBuilder() {
        super();
        this.metaMap = Maps.newHashMap();
        this.metaModifiers = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    protected MutableBuilder(@NonNull Iterable<? extends BuilderMeta<?, C>> metaIterable) {
        super();
        this.metaMap = Maps.newHashMap();
        this.metaModifiers = MultimapBuilder.hashKeys().arrayListValues().build();

        metaIterable.forEach(meta -> this.metaMap.put(meta.getKey(), meta.toMutable()));
    }

    protected MutableBuilder(@NonNull T object, @NonNull MetaResolver<T, C> resolver) {
        super();
        this.metaMap = Maps.newHashMap();
        this.metaModifiers = MultimapBuilder.hashKeys().arrayListValues().build();

        Map<BuilderMetaKey<?, C, ?, ?>, BuilderMeta<?, C>> resolved = resolver.resolve(object);
        resolved.forEach((key, value) -> this.metaMap.put(key, value.toMutable()));
    }

    public <V> SELF addMetaModifier(BuilderMetaKey<V, C, ? extends MutableBuilderMeta<V, C>, ?> metaKey, MetaModifier<V> valueModifier) {
        this.metaModifiers.put(metaKey, valueModifier);
        return this.getSelf();
    }

    public SELF setMetaMap(@NonNull Map<BuilderMetaKey<?, C, ?, ?>, MutableBuilderMeta<?, C>> metaMap) {
        this.metaMap = metaMap;
        return this.getSelf();
    }

    @Override
    public Collection<? extends BuilderMeta<?, C>> getMetaList() {
        return this.metaMap.values();
    }

    public <V, M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>> M getMeta(BuilderMetaKey<V, C, M, I> metaKey) {
        return (M) this.getMetaMap().get(metaKey);
    }

    public SELF addMeta(@NonNull MutableBuilderMeta<?, C> meta) {
        this.metaMap.put(meta.getKey(), meta);
        return this.getSelf();
    }

    public SELF addMetaIfNotExists(@NonNull MutableBuilderMeta<?, C> meta) {
        if (!this.metaMap.containsKey(meta.getKey())) {
            this.metaMap.put(meta.getKey(), meta);
        }
        return this.getSelf();
    }

    public <M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>, V> M getMetaOrDefault(
        BuilderMetaKey<V, C, M, I> metaKey, M defaultMeta) {
        return this.getMetaOrSupplyDefault(metaKey, () -> defaultMeta);
    }

    public <V, M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>> M getMetaOrSupplyDefault(
        BuilderMetaKey<V, C, M, I> metaKey, Supplier<M> defaultMeta) {
        M meta = this.getMeta(metaKey);
        if (meta == null) {
            this.addMeta(meta = defaultMeta.get());
        }
        return meta;
    }

    public <V, M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>> SELF modifyMeta(
        BuilderMetaKey<V, C, M, I> metaKey, Consumer<M> consumer) {
        M meta = this.getMeta(metaKey);
        if (meta == null) {
            this.metaMap.put(metaKey, meta = metaKey.createMutable());
        }
        consumer.accept(meta);
        return this.getSelf();
    }

}