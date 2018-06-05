package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.DescriptorImpl;

public abstract class QualifierProcessor<T extends Annotation> implements DescriptorProcessor {

    private final Class<T> tClass;

    protected QualifierProcessor(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public final DescriptorImpl process(ServiceLocator serviceLocator, @Nonnull DescriptorImpl descriptorImpl) {
        if (descriptorImpl.getQualifiers().contains(this.tClass.getName())) {
            return this.present(descriptorImpl);
        } else {
            return this.notPresent(descriptorImpl);
        }
    }

    protected String getMetadata(DescriptorImpl descriptor, String key) {
        return descriptor.getMetadata().get(key).get(0);
    }

    @Nullable
    protected abstract DescriptorImpl present(DescriptorImpl descriptor);

    @Nullable
    protected DescriptorImpl notPresent(DescriptorImpl descriptor) {
        return descriptor;
    }

}
