package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.repository.Repository;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import ninja.leaping.configurate.ConfigurationNode;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementRepositoryLoader {

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private Provider<ElementResolver> resolverProvider;

    @Inject
    private ElementDeserializerRegistry deserializerRegistry;

    @Nullable
    public Object loadFromRepository(ConfigElement<?> element, ConfigurationNode node)
        throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(node, "node");

        if (!element.hasAttribute(ElementAttributes.REPOSITORY_KEY)) {
            throw new IllegalArgumentException("element=" + element.toString() + " does not have the @LoadFromRepository annotation");
        }

        final LoadFromRepository repositoryAnnotation = element.getAttribute(ElementAttributes.REPOSITORY_KEY).get();

        final Class<? extends Repository<?, ?>> repositoryClass = repositoryAnnotation.value();
        final Repository repository;
        if (repositoryAnnotation.name().isEmpty()) {
            repository = this.serviceLocator.getService(repositoryClass);
        } else {
            repository = this.serviceLocator.getService(repositoryClass, repositoryAnnotation.name());
        }

        if (repository == null) {
            throw new IllegalStateException("element=" + element + " has repositoryClass=" + repositoryClass.getName()
                + ", repositoryName=" + repositoryAnnotation.name() + " but no Repository Service with that name could be found");
        }

        final ElementResolver elementResolver = this.resolverProvider.get();
        //TODO load from Map<?,?>, List<?>, and raw type
        return null;
    }

}