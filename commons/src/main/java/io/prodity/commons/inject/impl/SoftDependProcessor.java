package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.SoftDepend;
import javax.inject.Inject;
import org.glassfish.hk2.utilities.DescriptorImpl;

public class SoftDependProcessor extends AnnotationProcessor<SoftDepend> {

    private final Platform platform;

    @Inject
    public SoftDependProcessor(Platform platform) {
        super(SoftDepend.class);
        this.platform = platform;
    }

    @Override
    protected DescriptorImpl doProcess(DescriptorImpl descriptor, SoftDepend value) {
        return this.isSatisfied(value) ? descriptor : null;
    }

    private boolean isSatisfied(SoftDepend depend) {
        for (String plugin : depend.value()) {
            if (!this.platform.hasPlugin(plugin)) {
                return false;
            }
        }
        return true;
    }
}
