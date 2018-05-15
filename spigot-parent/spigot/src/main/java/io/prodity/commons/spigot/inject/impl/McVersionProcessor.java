package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.AnnotationProcessor;
import io.prodity.commons.spigot.inject.McVersion;
import org.glassfish.hk2.utilities.DescriptorImpl;

public class McVersionProcessor extends AnnotationProcessor<McVersion> {

    public McVersionProcessor() {
        super(McVersion.class);
    }

    @Override
    protected DescriptorImpl doProcess(DescriptorImpl descriptor, McVersion value) {
        return this.isSatisfied(value) ? descriptor : null;
    }

    private boolean isSatisfied(McVersion version) {
        for (String v : version.value()) {
            if (v.equalsIgnoreCase(McVersionFeature.CURRENT)) {
                return true;
            }
        }
        return false;
    }

}