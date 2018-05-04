package io.prodity.commons.plugin.annotate.process;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;

public class PluginDescription {

    private final Map<String, String> values;

    public PluginDescription() {
        this.values = Maps.newConcurrentMap();
    }

    public void set(@Nonnull String key, @Nonnull String value) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(value, "value");
        this.values.put(key, value);
    }

    @Nonnull
    public Map<String, String> getValuesCopy() {
        return Maps.newHashMap(this.values);
    }

}