package io.prodity.commons.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.identity.Identifiable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * An abstract implementation of {@link Repository} that relies on a {@link java.util.Map} supplied by the implementation.
 */
public abstract class SimpleRepository<K, V extends Identifiable<K>> implements Repository<K, V> {

    private final Map<K, V> map;
    private final TypeToken<K> keyType;
    private final TypeToken<V> valueType;

    protected SimpleRepository(Map<K, V> map, TypeToken<K> keyType, TypeToken<V> valueType) {
        Preconditions.checkNotNull(map, "map");
        Preconditions.checkNotNull(keyType, "keyType");
        Preconditions.checkNotNull(valueType, "valueType");
        this.map = map;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public TypeToken<K> getKeyType() {
        return this.keyType;
    }

    @Override
    public TypeToken<V> getValueType() {
        return this.valueType;
    }

    @Override
    public boolean containsId(@Nullable K key) {
        return key != null && this.map.containsKey(key);
    }

    @Nullable
    @Override
    public V get(@Nullable K key) {
        return key == null ? null : this.map.get(key);
    }

    @Override
    public Optional<V> getSafely(@Nullable K key) {
        return Optional.ofNullable(this.get(key));
    }

    @Override
    public Collection<V> getAsCollection(Iterable<K> keys) {
        Preconditions.checkNotNull(keys, "keys");
        final List<V> values = Lists.newArrayList();
        for (K key : keys) {
            if (key != null && this.map.containsKey(key)) {
                values.add(this.map.get(key));
            }
        }
        return values;
    }

    @Override
    public Map<K, V> getAsMap(Iterable<K> keys) {
        Preconditions.checkNotNull(keys, "keys");
        final Map<K, V> values = Maps.newHashMap();
        for (K key : keys) {
            if (key != null && this.map.containsKey(key)) {
                values.put(key, this.map.get(key));
            }
        }
        return values;
    }
    
}