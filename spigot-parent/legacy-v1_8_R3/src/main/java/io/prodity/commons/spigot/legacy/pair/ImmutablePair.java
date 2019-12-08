package io.prodity.commons.spigot.legacy.pair;

import com.google.common.base.Objects;
import lombok.Getter;

public class ImmutablePair<K, V> implements Pair<K, V> {

    public static <K, V> ImmutablePair<K, V> empty() {
        return new ImmutablePair<>(null, null);
    }

    public static <K, V> ImmutablePair<K, V> withKey(K key) {
        return new ImmutablePair<>(key, null);
    }

    public static <K, V> ImmutablePair<K, V> withValue(V value) {
        return new ImmutablePair<>(null, value);
    }

    public static <K, V> ImmutablePair<K, V> create(K key, V value) {
        return new ImmutablePair<>(key, value);
    }

    @Getter
    private final K key;
    @Getter
    private final V value;

    protected ImmutablePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key, this.value);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImmutablePair)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        final ImmutablePair pair = (ImmutablePair) object;
        return Objects.equal(this.key, pair.key) && Objects.equal(this.value, pair.value);
    }

}
