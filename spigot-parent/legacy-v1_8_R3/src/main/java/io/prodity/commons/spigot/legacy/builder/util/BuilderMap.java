package io.prodity.commons.spigot.legacy.builder.util;

import java.util.Map;

public interface BuilderMap<K, V, SELF extends BuilderMap<K, V, SELF>> extends Map<K, V> {

    default SELF getSelf() {
        return (SELF) this;
    }

    default SELF builderPut(K key, V value) {
        this.put(key, value);
        return this.getSelf();
    }

    default SELF builderPutIfAbsent(K key, V value) {
        this.putIfAbsent(key, value);
        return this.getSelf();
    }

    default SELF builderPutAll(Map<? extends K, ? extends V> map) {
        this.putAll(map);
        return this.getSelf();
    }

    default SELF builderRemove(K key, V value) {
        this.remove(key, value);
        return this.getSelf();
    }

    default SELF builderRemove(K key) {
        this.remove(key);
        return this.getSelf();
    }

}
