package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.plugin.ProdityPlugin;

public class ExportFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        bind(plugin, binder -> {
            binder.bind(ExportPostProcessor.class).to(DescriptorProcessor.class);
        });
    }

}
