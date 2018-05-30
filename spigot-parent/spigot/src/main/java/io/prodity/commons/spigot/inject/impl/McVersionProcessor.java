package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.QualifierProcessor;
import io.prodity.commons.spigot.inject.McVersion;
import org.glassfish.hk2.utilities.DescriptorImpl;

public class McVersionProcessor extends QualifierProcessor<McVersion> {

    public McVersionProcessor() {
        super(McVersion.class);
    }

    @Override
    protected DescriptorImpl present(DescriptorImpl descriptor) {
        String requiredVersion = this.getMetadata(descriptor, "McVersion");
        return this.isSatisfied(requiredVersion) ? descriptor : null;
    }

    private boolean isSatisfied(String version) {
        return McVersionFeature.CURRENT.equalsIgnoreCase(version);
    }

}