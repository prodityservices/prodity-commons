package io.prodity.commons.config.inject;

import io.prodity.commons.config.inject.deserialize.ElementResolver;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import javax.inject.Inject;
import javax.inject.Provider;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class ConfigInjectionContext {

    @Inject
    private Provider<ServiceLocator> serviceLocator;

    @Inject
    private Provider<ConfigInjector> injector;

    @Inject
    private Provider<ElementDeserializerRegistry> deserializerRegistry;

    @Inject
    private Provider<ElementResolver> elementResolver;

    public ServiceLocator getServiceLocator() {
        return this.serviceLocator.get();
    }

    public ConfigInjector getInjector() {
        return this.injector.get();
    }

    public ElementDeserializerRegistry getDeserializerRegistry() {
        return this.deserializerRegistry.get();
    }

    public ElementResolver getElementResolver() {
        return this.elementResolver.get();
    }

}