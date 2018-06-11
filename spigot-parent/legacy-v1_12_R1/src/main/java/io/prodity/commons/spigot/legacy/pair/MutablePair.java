package io.prodity.commons.spigot.legacy.pair;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

public class MutablePair<K, V> implements Pair<K, V> {

    public static <K, V> MutablePair<K, V> empty() {
        return new MutablePair<>(null, null);
    }

    public static <K, V> MutablePair<K, V> withKey(K key) {
        return new MutablePair<>(key, null);
    }

    public static <K, V> MutablePair<K, V> withValue(V value) {
        return new MutablePair<>(null, value);
    }

    public static <K, V> MutablePair<K, V> create(K key, V value) {
        return new MutablePair<>(key, value);
    }

    @Getter
    @Setter
    private K key;
    @Getter
    @Setter
    private V value;

    protected MutablePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key, this.value);
    }

    @Override
    public boolean equals(Object object) {
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