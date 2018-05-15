package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;

public class SoftDependFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, binder -> {
            binder.bind(SoftDependProcessor.class).to(DescriptorProcessor.class);
        });
    }
}
