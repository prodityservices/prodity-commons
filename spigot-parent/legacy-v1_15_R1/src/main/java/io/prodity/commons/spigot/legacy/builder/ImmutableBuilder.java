package io.prodity.commons.spigot.legacy.builder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.ImmutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.MutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import io.prodity.commons.spigot.legacy.builder.meta.resolve.MetaResolver;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;

public abstract class ImmutableBuilder<T, C extends BuilderConstruction<T>, SELF extends ImmutableBuilder<T, C, SELF>> extends
    AbstractBuilder<T, C, SELF> {

    @Getter
    private final ImmutableMap<BuilderMetaKey<?, C, ?, ?>, ? extends ImmutableBuilderMeta<?, C>> metaMap;

    @Getter
    private final ImmutableMultimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> metaModifiers;

    protected ImmutableBuilder() {
        super();
        this.metaModifiers = ImmutableMultimap.of();
        this.metaMap = ImmutableMap.of();
    }

    protected ImmutableBuilder(@NonNull Builder<T, C, ?> builder) {
        super(builder);

        final ImmutableMap.Builder<BuilderMetaKey<?, C, ?, ?>, ImmutableBuilderMeta<?, C>> mapBuilder = ImmutableMap.builder();
        builder.getMetaMap().forEach((key, meta) -> mapBuilder.put(key, meta.toImmutable()));
        this.metaMap = mapBuilder.build();
        this.metaModifiers = ImmutableMultimap.copyOf(builder.getMetaModifiers());
    }

    protected ImmutableBuilder(@NonNull Iterable<? extends BuilderMeta<?, C>> meta) {
        super();

        final ImmutableMap.Builder<BuilderMetaKey<?, C, ?, ?>, ImmutableBuilderMeta<?, C>> mapBuilder = ImmutableMap.builder();
        meta.forEach(metaValue -> mapBuilder.put(metaValue.getKey(), metaValue.toImmutable()));
        this.metaMap = mapBuilder.build();
        this.metaModifiers = ImmutableMultimap.of();
    }

    protected ImmutableBuilder(@NonNull T object, @NonNull MetaResolver<T, C> resolver) {
        super();
        final Map<BuilderMetaKey<?, C, ?, ?>, BuilderMeta<?, C>> resolved = resolver.resolve(object);

        final ImmutableMap.Builder<BuilderMetaKey<?, C, ?, ?>, ImmutableBuilderMeta<?, C>> mapBuilder = ImmutableMap.builder();
        resolved.forEach((key, value) -> mapBuilder.put(key, value.toImmutable()));
        this.metaMap = mapBuilder.build();
        this.metaModifiers = ImmutableMultimap.of();
    }

    public <M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>, V> I getMeta(BuilderMetaKey<V, C, M, I> metaKey) {
        return (I) this.getMetaMap().get(metaKey);
    }

    @Override
    public Collection<? extends BuilderMeta<?, C>> getMetaList() {
        return this.getMetaMap().values();
    }

}