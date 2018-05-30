package io.prodity.commons.config.inject.deserialize.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.inject.ConfigInjectionContext;
import io.prodity.commons.config.inject.deserialize.ElementDeserializer;
import io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry;
import io.prodity.commons.config.inject.deserialize.repository.ElementRepositoryType.ValueRetrieverData;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.repository.Repository;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementRepositoryResolver {

    private final List<ElementRepositoryType> supportedTypes = Lists.newCopyOnWriteArrayList();
    @Inject
    private ServiceLocator serviceLocator;
    @Inject
    private ElementDeserializerRegistry deserializerRegistry;
    @Inject
    private ConfigInjectionContext injectionContext;

    @PostConstruct
    private void registerDefaultTypes() {
        ElementRepositoryType.builder()
            .setPriority(Integer.MIN_VALUE)
            .setTokenPredicate((type) -> true)
            .setTypeVerifier((type, repository) -> {
                if (!type.isSupertypeOf(repository.getValueType())) {
                    throw new IllegalStateException(
                        "type=" + type + " does not match repository=" + repository.getClass().getName() + "'s valueType=" + repository
                            .getValueType());
                }
            })
            .setRepositoryKeyConverter((type) -> type)
            .setValueRetriever((data) -> data.getRepository().get(data.getKeyObject()))
            .register(this);

        ElementRepositoryType.createForCollection(0, new TypeToken<Collection<?>>() {}, Lists::newArrayList).register(this);
        ElementRepositoryType.createForCollection(1, new TypeToken<Set<?>>() {}, Sets::newHashSet).register(this);

        ElementRepositoryType.createForCollection(2, new TypeToken<ImmutableSet<?>>() {}, ImmutableSet::copyOf).register(this);
        ElementRepositoryType.createForCollection(2, new TypeToken<ImmutableList<?>>() {}, ImmutableList::copyOf).register(this);

        ElementRepositoryType.createForMap(0, new TypeToken<Map<?, ?>>() {}, Maps::newHashMap).register(this);
        ElementRepositoryType.createForMap(1, new TypeToken<ConcurrentMap<?, ?>>() {}, (values) -> {
            final Map map = Maps.newConcurrentMap();
            map.putAll(values);
            return map;
        }).register(this);

        ElementRepositoryType.createForMap(2, new TypeToken<ImmutableMap<?, ?>>() {}, ImmutableMap::copyOf).register(this);
    }

    void addSupportedType(ElementRepositoryType type) {
        Preconditions.checkNotNull(type, "type");
        this.supportedTypes.add(type);
        Collections.sort(this.supportedTypes, Comparator.reverseOrder());
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

        final ValueRetrieverData data = new ValueRetrieverData(element, repository, keyObject);
        final Object valueObject = repositoryType.getValue(data);

        return valueObject;
    }


}