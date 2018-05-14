package io.prodity.commons.pair;

import com.google.common.base.Preconditions;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface Pair<K, V> {

    K getKey();

    V getValue();

    default void accept(BiConsumer<K, V> biConsumer) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        biConsumer.accept(this.getKey(), this.getValue());
    }

    default <R> R apply(BiFunction<K, V, R> biFunction) {
        Preconditions.checkNotNull(biFunction, "biFunction");
        return biFunction.apply(this.getKey(), this.getValue());
    }

    default Map.Entry<K, V> toMapEntry() {
        return new AbstractMap.SimpleEntry<>(this.getKey(), this.getValue());
    }

}
