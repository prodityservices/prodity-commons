package io.prodity.commons.config.inject.deserialize.registry;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class ElementMapper<T, SELF extends ElementMapper<T, SELF>> {

    protected final ElementDeserializerRegistry deserializerRegistry;
    protected final TypeToken<T> typeToMap;
    protected int priority = ElementDeserializers.DEFAULT_PRIORITY + 1;
    protected BiPredicate<TypeToken<?>, TypeToken<?>> strategy = MapperStrategy.EQUALS;

    protected ElementMapper(ElementDeserializerRegistry deserializerRegistry, TypeToken<T> typeToMap) {
        Preconditions.checkNotNull(deserializerRegistry, "deserializerRegistry");
        Preconditions.checkNotNull(typeToMap, "typeToMap");
        this.deserializerRegistry = deserializerRegistry;
        this.typeToMap = typeToMap;
    }

    public ElementDeserializerRegistry getDeserializerRegistry() {
        return this.deserializerRegistry;
    }

    public TypeToken<T> getTypeToMap() {
        return this.typeToMap;
    }

    public int getPriority() {
        return this.priority;
    }

    public Predicate<TypeToken<?>> getStrategy() {
        return (type) -> this.strategy.test(this.typeToMap, type);
    }

    private SELF getSelf() {
        return (SELF) this;
    }

    /**
     * Sets the priority of the created {@link ElementMapper}. <br>
     * Default priority is {@link ElementDeserializers#DEFAULT_PRIORITY}+1
     *
     * @param priority the priority to set
     * @return this {@link ElementMapper} instance
     */
    public SELF withPriority(int priority) {
        this.priority = priority;
        return this.getSelf();
    }

    /**
     * Specifies the comparison strategy that is used in the generated {@link io.prodity.commons.config.inject.deserialize.ElementDeserializer}.<br>
     * Defaults to {@link MapperStrategy#EQUALS} if not specified.
     *
     * @param strategy the {@link BiPredicate} parameter the first argument should be assumed as {@link ElementMapper#getTypeToMap()}
     * @return this {@link ElementMapper} instance
     */
    public SELF withStrategy(BiPredicate<TypeToken<?>, TypeToken<?>> strategy) {
        this.strategy = strategy;
        return this.getSelf();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("typeToMap", this.typeToMap)
            .toString();
    }

    public enum MapperStrategy {
        ;

        /**
         * Strategy that returns true only if the two types are equal.
         */
        public static final BiPredicate<TypeToken<?>, TypeToken<?>> EQUALS = (token1, token2) -> token1.equals(token2);

        /**
         * Strategy that returns true if the type of the {@link ElementMapper} is a supertype of the specified token.<br>
         * In other words the specified type is a sub type of the {@link ElementMapper}'s type.
         */
        public static final BiPredicate<TypeToken<?>, TypeToken<?>> SUB_TYPE = (token1, token2) -> token1.isSupertypeOf(token2);

        /**
         * Strategy that returns true if the two raw types of the specified {@link TypeToken}s are equal.
         */
        public static final BiPredicate<TypeToken<?>, TypeToken<?>> RAW_TYPE = (token1, token2) -> token1.getRawType()
            .equals(token2.getRawType());


    }

}
