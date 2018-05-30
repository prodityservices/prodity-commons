package io.prodity.commons.inject.impl;

import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.IndexedFilter;

public class DependentFeatureFilter implements IndexedFilter {

    private final ProdityPlugin plugin;
    private final Class<?> tClass;

    public DependentFeatureFilter(ProdityPlugin plugin, Class<?> tClass) {
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
        String creator = InjectUtils.getOwner(d);
        if (creator != null) {
            return this.plugin.getSoftDependencies().contains(creator)
                || this.plugin.getDependencies().contains(creator)
                || creator.equals(this.plugin.getName());
        }
        return false;
    }
}
