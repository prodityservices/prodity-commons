package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.SoftDepend;
import javax.inject.Inject;
import org.glassfish.hk2.utilities.DescriptorImpl;

public class SoftDependProcessor extends QualifierProcessor<SoftDepend> {

    private final Platform platform;

    @Inject
    public SoftDependProcessor(Platform platform) {
        super(SoftDepend.class);
        this.platform = platform;
    }

    @Override
    protected DescriptorImpl present(DescriptorImpl descriptor) {
        String dependentOn = this.getMetadata(descriptor, "SoftDepend");
        return this.isSatisfied(dependentOn) ? descriptor : null;
    }

    private boolean isSatisfied(String depend) {
        return this.platform.hasPlugin(depend);
    }
}
