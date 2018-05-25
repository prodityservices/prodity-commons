package io.prodity.commons.config.inject.deserialize.repository;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.repository.Repository;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementRepositoryResolver {

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private ElementDeserializerRegistry deserializerRegistry;

    @Inject
    private ConfigInjectionContext injectionContext;

    private final Set<ElementRepositoryType> supportedTypes = new ConcurrentSkipListSet<>(Comparator.reverseOrder());

    @PostConstruct
    private void registerDefaultTypes() {
        final ElementRepositoryType singularObjectType = ElementRepositoryType.builder()
            .setPriority(Integer.MAX_VALUE)
            .setTokenPredicate((type) -> true)
            .setTypeVerifier((type, repository) -> {
                if (!type.isSupertypeOf(repository.getValueType())) {
                    throw new IllegalStateException(
                        "type=" + type + " does not match repository=" + repository.getClass().getName() + "'s valueType=" + repository
                            .getValueType());
                }
            })
            .setRepositoryKeyConverter((type) -> type)
            .setValueRetriever(Repository::get)
            .build();

        this.addSupportedType(singularObjectType);
    }

    public void addSupportedType(ElementRepositoryType type) {
        Preconditions.checkNotNull(type, "type");
        this.supportedTypes.add(type);
    }

    /**
     * Gets the {@link ElementRepositoryType} for the specified {@link TypeToken} is present.<br>
     * The type should typically be {@link ConfigElement} type that has the {@link LoadFromRepository} attribute.
     *
     * @param type the {@link TypeToken}
     * @return the {@link ElementRepositoryType} if present, else null
     */
    @Nullable
    public ElementRepositoryType getTypeFor(TypeToken<?> type) {
        Preconditions.checkNotNull(type, "type");
        for (ElementRepositoryType supportedType : this.supportedTypes) {
            if (supportedType.isTypeSupported(type)) {
                return supportedType;
            }
        }
        return null;
    }

    @Nullable
    public Object resolveFromRepository(ConfigElement<?> element, ConfigurationNode node)
        throws Throwable {
        Preconditions.checkNotNull(element, "element");
        Preconditions.checkNotNull(node, "node");

        if (!element.hasAttribute(ElementAttributes.REPOSITORY_KEY)) {
            throw new IllegalArgumentException("element=" + element + " does not have the @LoadFromRepository annotation");
        }

        final LoadFromRepository repositoryAnnotation = element.getAttribute(ElementAttributes.REPOSITORY_KEY).get();

        final Class<? extends Repository<?, ?>> repositoryClass = repositoryAnnotation.value();
        final Repository<?, ?> repository;
        if (repositoryAnnotation.name().isEmpty()) {
            repository = this.serviceLocator.getService(repositoryClass);
        } else {
            repository = this.serviceLocator.getService(repositoryClass, repositoryAnnotation.name());
        }

        if (repository == null) {
            throw new IllegalStateException("element=" + element + " has repositoryClass=" + repositoryClass.getName()
                + ", repositoryName=" + repositoryAnnotation.name() + " but no Repository Service with that name could be found");
        }

        final TypeToken<?> elementType = element.getType();
        final ElementRepositoryType repositoryType = this.getTypeFor(elementType);
        if (repositoryType == null) {
            throw new IllegalStateException("element=" + element + " is not a valid type for @LoadFromRepository");
        }

        repositoryType.verifyType(elementType, repository);

        final TypeToken<?> keyType = repositoryType.convertRepositoryKey(repository.getKeyType());
        final ElementDeserializer<?> keyDeserializer = this.deserializerRegistry.get(keyType);

        final Object keyObject = keyDeserializer.deserialize(this.injectionContext, keyType, node);
        final Object valueObject = repositoryType.getValue(repository, keyObject);

        return valueObject;
    }


}