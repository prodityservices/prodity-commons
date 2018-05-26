package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.Export;
import io.prodity.commons.plugin.ProdityPlugin;
import javax.inject.Inject;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.utilities.DescriptorImpl;

public class ExportPostProcessor extends QualifierProcessor<Export> {

    private final ProdityPlugin plugin;

    @Inject
    public ExportPostProcessor(ProdityPlugin plugin) {
        super(Export.class);
        this.plugin = plugin;
    }

    @Override
    protected DescriptorImpl present(DescriptorImpl descriptor) {
        descriptor.setDescriptorVisibility(DescriptorVisibility.NORMAL);
        descriptor.addMetadata(Export.PLUGIN_META_KEY, this.plugin.getName());
        return descriptor;
    }

    @Override
    protected DescriptorImpl notPresent(DescriptorImpl descriptor) {
        descriptor.setDescriptorVisibility(DescriptorVisibility.LOCAL);
        descriptor.addMetadata(Export.PLUGIN_META_KEY, this.plugin.getName());
        return descriptor;
    }

}