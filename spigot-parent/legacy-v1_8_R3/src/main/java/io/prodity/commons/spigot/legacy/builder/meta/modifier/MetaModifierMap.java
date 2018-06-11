package io.prodity.commons.spigot.legacy.builder.meta.modifier;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multiset;
import io.prodity.commons.spigot.legacy.builder.construct.BuilderConstruction;
import io.prodity.commons.spigot.legacy.builder.meta.BuilderMetaKey;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.Getter;

public class MetaModifierMap<C extends BuilderConstruction<?>> implements Multimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> {

    public static <C extends BuilderConstruction<?>> MetaModifierMap<C> create() {
        return new MetaModifierMap<>();
    }

    @Getter
    private final Multimap<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>> modifiers;

    private MetaModifierMap() {
        this.modifiers = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    @Override
    public int size() {
        return this.modifiers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.modifiers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.modifiers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.modifiers.containsValue(value);
    }

    @Override
    public boolean containsEntry(Object key, Object value) {
        return this.modifiers.containsEntry(key, value);
    }

    public <V> MetaModifierMap<C> builderPut(BuilderMetaKey<V, C, ?, ?> cBuilderMetaKey, MetaModifier<V> metaModifier) {
        this.modifiers.put(cBuilderMetaKey, metaModifier);
        return this;
    }

    @Override
    @Deprecated
    public boolean put(BuilderMetaKey<?, C, ?, ?> cBuilderMetaKey, MetaModifier<?> metaModifier) {
        throw new UnsupportedOperationException("use builderPut");
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.modifiers.remove(key, value);
    }

    @Override
    @Deprecated
    public boolean putAll(BuilderMetaKey<?, C, ?, ?> cBuilderMetaKey, Iterable<? extends MetaModifier<?>> iterable) {
        throw new UnsupportedOperationException("use builderPutAll");
    }

    public <V> MetaModifierMap<C> builderPutAll(BuilderMetaKey<V, C, ?, ?> cBuilderMetaKey, Iterable<? extends MetaModifier<V>> iterable) {
        this.modifiers.putAll(cBuilderMetaKey, iterable);
        return this;
    }

    @Override
    public boolean putAll(Multimap<? extends BuilderMetaKey<?, C, ?, ?>, ? extends MetaModifier<?>> multimap) {
        return this.modifiers.putAll(multimap);
    }

    public MetaModifierMap<C> builderPutAll(Multimap<? extends BuilderMetaKey<?, C, ?, ?>, ? extends MetaModifier<?>> multimap) {
        this.putAll(multimap);
        return this;
    }

    @Override
    public Collection<MetaModifier<?>> replaceValues(BuilderMetaKey<?, C, ?, ?> cBuilderMetaKey,
        Iterable<? extends MetaModifier<?>> iterable) {
        return this.modifiers.replaceValues(cBuilderMetaKey, iterable);
    }

    @Override
    public Collection<MetaModifier<?>> removeAll(Object o) {
        return this.modifiers.removeAll(o);
    }

    @Override
    public void clear() {
        this.modifiers.clear();
    }

    @Override
    public Collection<MetaModifier<?>> get(BuilderMetaKey<?, C, ?, ?> cBuilderMetaKey) {
        return this.modifiers.get(cBuilderMetaKey);
    }

    @Override
    public Set<BuilderMetaKey<?, C, ?, ?>> keySet() {
        return this.modifiers.keySet();
    }

    @Override
    public Multiset<BuilderMetaKey<?, C, ?, ?>> keys() {
        return this.modifiers.keys();
    }

    @Override
    public Collection<MetaModifier<?>> values() {
        return this.modifiers.values();
    }

    @Override
    public Collection<Entry<BuilderMetaKey<?, C, ?, ?>, MetaModifier<?>>> entries() {
        return this.modifiers.entries();
    }

    @Override
    public Map<BuilderMetaKey<?, C, ?, ?>, Collection<MetaModifier<?>>> asMap() {
        return this.modifiers.asMap();
    }

}