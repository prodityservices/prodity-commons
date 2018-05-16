package io.prodity.commons.pair;

import java.util.Objects;
import javax.annotation.Nullable;

public class ImmutablePair<K, V> implements Pair<K, V> {

    public static <K, V> ImmutablePair<K, V> empty() {
        return new ImmutablePair<>(null, null);
    }

    public static <K, V> ImmutablePair<K, V> withKey(@Nullable K key) {
        return new ImmutablePair<>(key, null);
    }

    public static <K, V> ImmutablePair<K, V> withValue(@Nullable V value) {
        return new ImmutablePair<>(null, value);
    }

    public static <K, V> ImmutablePair<K, V> with(@Nullable K key, @Nullable V value) {
        return new ImmutablePair<>(key, value);
    }

    private final K key;
    private final V value;

    protected ImmutablePair(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    @Nullable
    public K getKey() {
        return this.key;
    }

    @Override
    @Nullable
    public V getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (!(object instanceof ImmutablePair)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        final ImmutablePair pair = (ImmutablePair) object;
        return Objects.equals(this.key, pair.key) && Objects.equals(this.value, pair.value);
    }

}
