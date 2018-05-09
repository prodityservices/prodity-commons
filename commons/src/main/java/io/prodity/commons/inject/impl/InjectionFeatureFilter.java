package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.IndexedFilter;

public class InjectionFeatureFilter implements IndexedFilter {
    private final ProdityPlugin plugin;

    public InjectionFeatureFilter(ProdityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAdvertisedContract() {
        return InjectionFeature.class.getName();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean matches(Descriptor d) {
        if (d.getDescriptorVisibility() == DescriptorVisibility.NORMAL) {
            String creator = InjectUtils.getOwner(d);
            if (creator != null) {
                return plugin.getSoftDependencies().contains(creator)
                        || plugin.getDependencies().contains(creator);
            }
        }
        return false;
    }
}
