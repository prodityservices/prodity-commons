package io.prodity.commons.inject.impl;

import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.IndexedFilter;

public class LocalServiceFilter<T> implements IndexedFilter {

    private final ProdityPlugin plugin;
    private final Class<T> tClass;

    public LocalServiceFilter(ProdityPlugin plugin, Class<T> tClass) {
        this.plugin = plugin;
        this.tClass = tClass;
    }

    @Override
    public String getAdvertisedContract() {
        return this.tClass.getName();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean matches(Descriptor d) {
        return d.getDescriptorVisibility() == DescriptorVisibility.LOCAL
            || InjectUtils.isFromPlugin(d, this.plugin);
    }

}
