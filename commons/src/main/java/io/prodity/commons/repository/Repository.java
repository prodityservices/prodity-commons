package io.prodity.commons.repository;

import io.prodity.commons.identity.Identifiable;
import io.prodity.commons.name.Named;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface Repository<K, V extends Identifiable<K>> extends Named {

    boolean containsId(K key);

    V get(K key);

    Optional<V> getSafely(K key);

    Collection<V> getAsCollection(Iterable<K> keys);

    Map<K, V> getAsMap(Iterable<K> keys);

}