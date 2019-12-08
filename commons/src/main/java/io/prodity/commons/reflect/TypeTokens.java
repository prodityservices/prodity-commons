package io.prodity.commons.reflect;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import java.util.List;
import java.util.Map;

/**
 * Utilities for {@link com.google.common.reflect.TypeToken}s.
 */
public enum TypeTokens {

    ;

    public static <K, V> TypeToken<Map<K, V>> mapToken(TypeToken<K> keyToken, TypeToken<V> valueToken) {
        Preconditions.checkNotNull(keyToken, "keyToken");
        Preconditions.checkNotNull(valueToken, "valueToken");
        return new TypeToken<Map<K, V>>() {}
            .where(new TypeParameter<K>() {}, keyToken)
            .where(new TypeParameter<V>() {}, valueToken);
    }

    public static <E> TypeToken<List<E>> listToken(TypeToken<E> elementToken) {
        Preconditions.checkNotNull(elementToken, "elementToken");
        return new TypeToken<List<E>>() {}
            .where(new TypeParameter<E>() {}, elementToken);
    }

}