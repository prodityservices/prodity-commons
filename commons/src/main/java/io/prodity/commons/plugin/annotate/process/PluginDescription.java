package io.prodity.commons.plugin.annotate.process;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public class PluginDescription {

    private final Map<String, String> values;

    public PluginDescription() {
        this.values = Maps.newConcurrentMap();
    }

    public void set(String key, String value) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(value, "deserialize");
        this.values.put(key, value);
    }

    public Map<String, String> getValuesCopy() {
        return Maps.newHashMap(this.values);
    }

}