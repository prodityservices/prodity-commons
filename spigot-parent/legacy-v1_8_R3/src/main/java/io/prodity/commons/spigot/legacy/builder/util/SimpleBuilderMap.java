package io.prodity.commons.spigot.legacy.builder.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleBuilderMap<K, V, SELF extends BuilderMap<K, V, SELF>> extends HashMap<K, V> implements
    BuilderMap<K, V, SELF> {

    public SimpleBuilderMap() {
        super();
    }

    public SimpleBuilderMap(Map<K, V> map) {
        super(map);
    }

}
