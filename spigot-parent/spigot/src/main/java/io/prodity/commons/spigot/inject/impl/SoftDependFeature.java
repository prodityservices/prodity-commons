package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;

public class SoftDependFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        bind(plugin, binder -> {
            binder.bind(SoftDependProcessor.class).to(DescriptorProcessor.class);
        });
    }
}