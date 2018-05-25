package io.prodity.commons.config.inject.deserialize.repository;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.annotate.deserialize.LoadFromRepository;
import io.prodity.commons.config.inject.element.ConfigElement;
import io.prodity.commons.repository.Repository;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
        private BiFunction<Repository, Object, Object> valueRetriever;

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
         * Sets the {@link BiFunction} that retrieves value(s) from a {@link Repository} using an untyped key object.
         *
         * @param valueRetriever the {@link BiFunction}
         * @return this builder instance
         */
        public Builder setValueRetriever(BiFunction<Repository, Object, Object> valueRetriever) {
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

        public ElementRepositoryType build() {
            this.verify();
            return new ElementRepositoryType(this.priority, this.tokenPredicate, this.typeVerifier, this.repositoryKeyConverter,
                this.valueRetriever);
        }

    }

    public static ElementRepositoryType.Builder builder() {
        return new ElementRepositoryType.Builder();
    }

    private final int priority;
    private final Predicate<TypeToken<?>> tokenPredicate;
    private final BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier;
    private final Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter;
    private final BiFunction<Repository, Object, Object> valueRetriever;

    private ElementRepositoryType(int priority, Predicate<TypeToken<?>> tokenPredicate,
        BiConsumer<TypeToken<?>, Repository<?, ?>> typeVerifier, Function<TypeToken<?>, TypeToken<?>> repositoryKeyConverter,
        BiFunction<Repository, Object, Object> valueRetriever) {
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
     * Gets the value(s) from the specified {@link Repository} and keyObject.<br>
     * The keyObject is typically a singular key or multiple (in a {@link java.util.Collection}) depending on this implementation.
     *
     * @param repository the {@link Repository}
     * @param keyObject the key(s)
     * @return the value(s)
     */
    public Object getValue(Repository repository, Object keyObject) {
        return this.valueRetriever.apply(repository, keyObject);
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