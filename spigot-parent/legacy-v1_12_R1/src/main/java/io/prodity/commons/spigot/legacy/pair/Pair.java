package io.prodity.commons.spigot.legacy.pair;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public interface Pair<K, V> {

    K getKey();

    V getValue();

    default Entry<K, V> toMapEntry() {
        return new SimpleEntry<>(this.getKey(), this.getValue());
    }

}
