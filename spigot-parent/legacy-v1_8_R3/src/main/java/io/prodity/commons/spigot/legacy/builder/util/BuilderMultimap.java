package io.prodity.commons.spigot.legacy.builder.util;

import com.google.common.collect.Multimap;

public interface BuilderMultimap<K, V, SELF extends BuilderMultimap<K, V, SELF>> extends Multimap<K, V> {

    default SELF getSelf() {
        return (SELF) this;
    }

    default SELF builderPut(K key, V value) {
        this.put(key, value);
        return this.getSelf();
    }

    default SELF builderPutAll(Multimap<? extends K, ? extends V> map) {
        this.putAll(map);
        return this.getSelf();
    }

    default SELF builderPutAll(K key, Iterable<? extends V> values) {
        this.putAll(key, values);
        return this.getSelf();
    }

    default SELF builderRemove(K key, V value) {
        this.remove(key, value);
        return this.getSelf();
    }

    default SELF builderRemoveAll(K key) {
        this.removeAll(key);
        return this.getSelf();
    }

}