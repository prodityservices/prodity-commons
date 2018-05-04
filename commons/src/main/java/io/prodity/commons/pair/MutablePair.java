package io.prodity.commons.pair;

import com.google.common.base.Objects;
import javax.annotation.Nullable;

public class MutablePair<K, V> implements Pair<K, V> {

    public static <K, V> MutablePair<K, V> empty() {
        return new MutablePair<>(null, null);
    }

    public static <K, V> MutablePair<K, V> withKey(@Nullable K key) {
        return new MutablePair<>(key, null);
    }

    public static <K, V> MutablePair<K, V> withValue(@Nullable V value) {
        return new MutablePair<>(null, value);
    }

    public static <K, V> MutablePair<K, V> with(@Nullable K key, @Nullable V value) {
        return new MutablePair<>(key, value);
    }

    private K key;
    private V value;

    protected MutablePair(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    @Nullable
    public K getKey() {
        return this.key;
    }

    public void setKey(@Nullable K key) {
        this.key = key;
    }

    @Override
    @Nullable
    public V getValue() {
        return this.value;
    }

    public void setValue(@Nullable V value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key, this.value);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (!(object instanceof MutablePair)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        final MutablePair pair = (MutablePair) object;
        return Objects.equal(this.key, pair.key) && Objects.equal(this.value, pair.value);
    }

}