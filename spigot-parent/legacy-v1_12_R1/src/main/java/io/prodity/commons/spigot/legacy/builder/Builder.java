package io.prodity.commons.spigot.legacy.builder;

import com.google.common.collect.Multimap;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import io.prodity.commons.spigot.legacy.builder.meta.ImmutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.MutableBuilderMeta;
import io.prodity.commons.spigot.legacy.builder.meta.modifier.MetaModifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Builder<T, C extends BuilderConstruction<T>, SELF extends Builder<T, C, SELF>> extends Cloneable {

    default SELF getSelf() {
        return (SELF) this;
    }

    <M extends MutableBuilderMeta<V, C>, I extends ImmutableBuilderMeta<V, C>, V> V getMetaValue(BuilderMetaKey<V, C, M, I> key);

    Collection<? extends BuilderMeta<?, C>> getMetaList();

    Map<BuilderMetaKey<?, C, ?, ?>, ? extends BuilderMeta<?, C>> getMetaMap();

    Multimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> getMetaModifiers();

    boolean containsMeta(BuilderMeta<?, C> meta);

    boolean containsMetaKey(BuilderMetaKey<?, C, ?, ?> key);

    SELF addPostConstructHandler(Function<T, T> function);

    List<Function<T, T>> getPostConstructHandlers();

    SELF setPostConstructHandlers(List<Function<T, T>> postConstructionHandlers);

    T build();

    MutableBuilder<T, C, ?> toMutable();

    ImmutableBuilder<T, C, ?> toImmutable();

}