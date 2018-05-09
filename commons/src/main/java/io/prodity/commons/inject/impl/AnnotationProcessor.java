package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import java.lang.annotation.Annotation;
import org.glassfish.hk2.api.DescriptorType;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.DescriptorImpl;

public abstract class AnnotationProcessor<T extends Annotation> implements DescriptorProcessor {

    private final Class<T> tClass;

    protected AnnotationProcessor(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public final DescriptorImpl process(ServiceLocator serviceLocator, DescriptorImpl descriptorImpl) {
        if (descriptorImpl.getDescriptorType() == DescriptorType.CLASS) {
            try {
                Class<?> clazz = Class.forName(descriptorImpl.getImplementation());
                T annotation = clazz.getAnnotation(tClass);
                if (annotation != null) {
                    return doProcess(descriptorImpl, annotation);
                } else {
                    return notPresent(descriptorImpl);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return descriptorImpl;
    }

    protected abstract DescriptorImpl doProcess(DescriptorImpl descriptor, T value);

    protected DescriptorImpl notPresent(DescriptorImpl descriptor) {
        return descriptor;
    }
}
