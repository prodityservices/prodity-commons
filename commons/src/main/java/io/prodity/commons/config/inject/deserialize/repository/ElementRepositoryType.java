package io.prodity.commons.config.inject.deserialize.repository;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.config.inject.element.attribute.ElementAttributes;
import io.prodity.commons.identity.Identifiable;
import io.prodity.commons.reflect.TypeTokens;
import io.prodity.commons.repository.Repository;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a supported {@link ConfigElement} type that is supported by the {@link LoadFromRepository} element attribute(annotation).
 */
public class ElementRepositoryType implements Comparable<ElementRepositoryType> {

    public static final class Builder {

        private Integer priority;
        private Predicate<TypeToken<?>> tokenPredicate;
        private BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier;
        private Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter;
        private Function<ValueRetrieverData, Object> valueRetriever;

        private Builder() {

        }

        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Sets the {@link Predicate} that is used to verify is a {@link ConfigElement}'s type is supported by the created {@link ElementRepositoryType}.
         *
         * @param tokenPredicate the {@link Predicate}
         * @return this builder instance
         */
        public Builder setTokenPredicate(Predicate<TypeToken<?>> tokenPredicate) {
            this.tokenPredicate = tokenPredicate;
            return this;
        }

        /**
         * Sets the {@link BiConsumer} that is used in verifying a {@link ConfigElement}'s type(s) are equal to the {@link Repository} it is being loaded from.
         *
         * @param typeVerifier the {@link BiConsumer}
         * @return this builder instance
         */
        public Builder setTypeVerifier(BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier) {
            this.typeVerifier = typeVerifier;
            return this;
        }

        /**
         * Sets the {@link Function} that converts {@link Repository#getKeyType()}s to the {@link TypeToken} to be deserialized.
         *
         * @param repositoryKeyConverter the {@link Function}
         * @return this builder instance
         */
        public Builder setRepositoryKeyConverter(Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter) {
            this.repositoryKeyConverter = repositoryKeyConverter;
            return this;
        }

        /**
         * Sets the {@link Function} that retrieves value(s) from a {@link Repository} using an untyped key object.
         *
         * @param valueRetriever the {@link Function}
         * @return this builder instance
         */
        public Builder setValueRetriever(Function<ValueRetrieverData, Object> valueRetriever) {
            this.valueRetriever = valueRetriever;
            return this;
        }

        private void verify() {
            Preconditions.checkNotNull(this.priority, "priority");
            Preconditions.checkNotNull(this.tokenPredicate, "tokenPredicate");
            Preconditions.checkNotNull(this.typeVerifier, "typeVerifier");
            Preconditions.checkNotNull(this.repositoryKeyConverter, "repositoryKeyConverter");
            Preconditions.checkNotNull(this.valueRetriever, "valueRetriever");
        }

        private ElementRepositoryType build() {
            this.verify();
            return new ElementRepositoryType(this.priority, this.tokenPredicate, this.typeVerifier, this.repositoryKeyConverter,
                this.valueRetriever);
        }

        public void register(ElementRepositoryResolver resolver) {
            Preconditions.checkNotNull(resolver, "resolver");
            final ElementRepositoryType type = this.build();
            resolver.addSupportedType(type);
        }

        @Override
        public ElementRepositoryType.Builder clone() {
            final ElementRepositoryType.Builder builder = new ElementRepositoryType.Builder();
            if (this.priority != null) {
                builder.setPriority(this.priority);
            }

            builder.setTokenPredicate(this.tokenPredicate);
            builder.setTypeVerifier(this.typeVerifier);
            builder.setRepositoryKeyConverter(this.repositoryKeyConverter);
            builder.setValueRetriever(this.valueRetriever);

            return builder;
        }

    }

    public static final class ValueRetrieverData {

        private final ConfigElement<?> element;
        private final Repository repository;
        private final Object keyObject;

        public ValueRetrieverData(ConfigElement<?> element, Repository repository, Object keyObject) {
            Preconditions.checkNotNull(element, "element");
            Preconditions.checkNotNull(repository, "repository");
            Preconditions.checkNotNull(keyObject, "keyObject");
            this.element = element;
            this.repository = repository;
            this.keyObject = keyObject;
        }

        public ConfigElement<?> getElement() {
            return this.element;
        }

        public Repository getRepository() {
            return this.repository;
        }

        public Object getKeyObject() {
            return this.keyObject;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("element", this.element)
                .add("repository", this.repository)
                .add("keyObject", this.keyObject)
                .toString();
        }

    }

    public static ElementRepositoryType.Builder builder() {
        return new ElementRepositoryType.Builder();
    }

    /**
     * Creates a new {@link ElementRepositoryType.Builder} for a {@link Collection} type.
     *
     * @param priority the priority
     * @param collectionType the {@link TypeToken} of the {@link Collection} implementation
     * @param collectionConverter the {@link Function} that converts the {@link Collection} of resolved
     * values to the {@link Collection} implementation to be used. This is only used if the specified type can
     * not be instantiated via reflection.
     * @return the created {@link ElementRepositoryType.Builder}
     */
    public static ElementRepositoryType.Builder createForCollection(int priority, TypeToken<? extends Collection<?>> collectionType,
        Function<Collection<?>, Collection<?>> collectionConverter) {

        return ElementRepositoryType.builder()
            .setPriority(priority)
            .setTokenPredicate((type) -> type.isSubtypeOf(collectionType))
            .setTypeVerifier((type, repository) -> {
                final TypeToken<?> elementType = type.resolveType(Collection.class.getTypeParameters()[0]);
                if (!elementType.isSupertypeOf(repository.getValueType())) {
                    throw new IllegalStateException(
                        "elementType=" + elementType + " of type=" + type + " does not match repository=" + repository.getClass().getName()
                            + "'s valueType=" + repository.getValueType());
                }
            })
            .setRepositoryKeyConverter(TypeTokens::listToken)
            .setValueRetriever((data) -> {
                final ConfigElement<?> element = data.getElement();

                final Collection<?> keyCollection = (Collection<?>) data.getKeyObject();
                final List values = Lists.newArrayList();
                for (Object key : keyCollection) {
                    final Object value = data.getRepository().get(key);
                    if (value != null) {
                        values.add(value);
                        continue;
                    }
                    if (element.hasAttribute(ElementAttributes.REQUIRED_KEY)) {
                        throw new IllegalStateException("data=" + data + " has key=" + key + " that is missing from the Repository");
                    }
                }

                try {
                    final Class<?> collectionClass = element.getType().getRawType();
                    try {
                        for (Constructor<?> constructor : collectionClass.getDeclaredConstructors()) {
                            if (constructor.getParameterCount() != 1 || !constructor.getParameterTypes()[0]
                                .isAssignableFrom(List.class)) {
                                continue;
                            }
                            return constructor.newInstance(values);
                        }
                        throw new Throwable();
                    } catch (Throwable throwable) {
                        final Collection<?> instance = (Collection<?>) collectionClass.newInstance();
                        instance.addAll(values);
                        return instance;
                    }
                } catch (Throwable throwable) {}
                return collectionConverter.apply(values);
            });
    }

    /**
     * Creates a new {@link ElementRepositoryType.Builder} for a {@link Map} type.
     *
     * @param priority the priority
     * @param mapType the {@link TypeToken} of the {@link Map} implementation
     * @param mapConverter the {@link Function} that converts the {@link Map} of resolved
     * values to the {@link Map} implementation to be used. This is only used if the specified type can
     * not be instantiated via reflection.
     * @return the created {@link ElementRepositoryType.Builder}
     */
    public static ElementRepositoryType.Builder createForMap(int priority, TypeToken<? extends Map<?, ?>> mapType,
        Function<Map<?, ?>, Map<?, ?>> mapConverter) {
        return ElementRepositoryType.builder()
            .setPriority(priority)
            .setTokenPredicate((type) -> type.isSubtypeOf(mapType))
            .setTypeVerifier((type, repository) -> {
                final TypeToken<?> keyType = type.resolveType(Map.class.getTypeParameters()[0]);
                if (!keyType.isSupertypeOf(repository.getKeyType())) {
                    throw new IllegalStateException(
                        "type=" + type + "'s key Type does not match repository=" + repository.getClass().getName() + "'s keyType="
                            + repository
                            .getKeyType());
                }
                final TypeToken<?> valueType = type.resolveType(Map.class.getTypeParameters()[1]);
                if (!valueType.isSupertypeOf(repository.getValueType())) {
                    throw new IllegalStateException(
                        "type=" + type + "'s value Type does not match repository=" + repository.getClass().getName() + "'s valueType="
                            + repository
                            .getValueType());
                }
            })
            .setRepositoryKeyConverter(TypeTokens::listToken)
            .setValueRetriever((data) -> {
                final ConfigElement<?> element = data.getElement();

                final Collection<?> keyCollection = (Collection<?>) data.getKeyObject();
                final Map values = Maps.newHashMap();
                for (Object key : keyCollection) {
                    final Identifiable<?> value = data.getRepository().get(key);
                    if (value != null) {
                        values.put(value.getId(), value);
                        continue;
                    }
                    if (element.hasAttribute(ElementAttributes.REQUIRED_KEY)) {
                        throw new IllegalStateException("data=" + data + " has key=" + key + " that is missing from the Repository");
                    }
                }

                try {
                    final Class<?> mapClass = element.getType().getRawType();
                    try {
                        for (Constructor<?> constructor : mapClass.getDeclaredConstructors()) {
                            if (constructor.getParameterCount() != 1 || !constructor.getParameterTypes()[0]
                                .isAssignableFrom(Map.class)) {
                                continue;
                            }
                            return constructor.newInstance(values);
                        }
                    } catch (Throwable throwable) {
                        final Map<?, ?> instance = (Map<?, ?>) mapClass.newInstance();
                        instance.putAll(values);
                        return instance;
                    }
                } catch (Throwable throwable) {}
                return mapConverter.apply(values);
            });
    }


    private final int priority;
    private final Predicate<TypeToken<?>> tokenPredicate;
    private final BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier;
    private final Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter;
    private final Function<ValueRetrieverData, Object> valueRetriever;

    private ElementRepositoryType(int priority, Predicate<TypeToken<?>> tokenPredicate,
        BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier, Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter,
        Function<ValueRetrieverData, Object> valueRetriever) {
        Preconditions.checkNotNull(tokenPredicate, "tokenPredicate");
        Preconditions.checkNotNull(typeVerifier, "typeVerifier");
        Preconditions.checkNotNull(repositoryKeyConverter, "repositoryKeyConverter");
        Preconditions.checkNotNull(valueRetriever, "valueRetriever");
        this.priority = priority;
        this.tokenPredicate = tokenPredicate;
        this.typeVerifier = typeVerifier;
        this.repositoryKeyConverter = repositoryKeyConverter;
        this.valueRetriever = valueRetriever;
    }

    public int getPriority() {
        return this.priority;
    }

    /**
     * Gets whether or not the specified {@link TypeToken} is supported by this {@link ElementRepositoryType}.
     *
     * @param typeToken the type
     * @return true if supported, false if not
     */
    public boolean isTypeSupported(TypeToken<?> typeToken) {
        Preconditions.checkNotNull(typeToken, "typeToken");
        return this.tokenPredicate.test(typeToken);
    }

    /**
     * Verifies that the specified {@link TypeToken} matches the specified {@link Repository} instance.<br>
     * This is mostly to ensure that the generic types (& type parameters) of types such as Maps/Lists equal
     * those of the {@link Repository}'s generic type parameters.
     *
     * @param type the {@link TypeToken}
     * @param repository the {@link Repository}
     * @throws RuntimeException if the specified {@link TypeToken} does not match the types of the {@link Repository}
     */
    public void verifyType(TypeToken<?> type, Repository<?, ?> repository) throws RuntimeException {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(repository, "repository");
        this.typeVerifier.accept(type, repository);
    }

    /**
     * Converts the specified {@link Repository#getKeyType()} to the {@link TypeToken} that will be used when the {@link ElementRepositoryResolver} deserializes.
     *
     * @param repositoryKey the repository's key type
     * @return the converted {@link TypeToken}
     */
    public TypeToken<?> convertRepositoryKey(TypeToken<?> repositoryKey) {
        Preconditions.checkNotNull(repositoryKey, "repositoryKey");
        return this.repositoryKeyConverter.apply(repositoryKey);
    }

    /**
     * Gets the value(s) from the specified {@link ValueRetrieverData}.<br>
     * The {@link ValueRetrieverData#getKeyObject()} is typically a singular key or multiple (in a {@link java.util.Collection}) depending on this implementation.
     *
     * @param data the {@link ValueRetrieverData)
     * @return the value(s)
     */
    public Object getValue(ValueRetrieverData data) {
        return this.valueRetriever.apply(data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.priority, this.tokenPredicate, this.typeVerifier, this.repositoryKeyConverter);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ElementRepositoryType)) {
            return false;
        }
        final ElementRepositoryType that = (ElementRepositoryType) object;
        return Objects.equals(this.priority, that.priority) &&
            Objects.equals(this.tokenPredicate, that.tokenPredicate) &&
            Objects.equals(this.typeVerifier, that.typeVerifier) &&
            Objects.equals(this.repositoryKeyConverter, that.repositoryKeyConverter);
    }

    @Override
    public int compareTo(ElementRepositoryType that) {
        return Integer.compare(this.priority, that.priority);
    }

}