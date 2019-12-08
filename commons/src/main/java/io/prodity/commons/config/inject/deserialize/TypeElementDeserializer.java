package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;

/**
 * {@link ElementDeserializer} implementation for an explicit {@link TypeToken}.
 *
 * @param <T> the type to be deserialized
 */
public abstract class TypeElementDeserializer<T> extends ElementDeserializer<T> {

    private final TypeToken<T> typeToken;
    private final boolean suppportChildTypes;

    public TypeElementDeserializer(TypeToken<T> typeToken, int priority) {
        this(typeToken, priority, false);
    }

    /**
     * @param typeToken the {@link TypeToken} of the supported type
     * @param priority the priority to be used in {@link io.prodity.commons.config.inject.deserialize.registry.ElementDeserializerRegistry}s
     * @param suppportChildTypes whether or not to support child(sub) types of the specified type.
     * If true is specified, any types that extend the supported type can be deserialized byWithType this deserializer.
     */
    public TypeElementDeserializer(TypeToken<T> typeToken, int priority, boolean suppportChildTypes) {
        super(priority);
        Preconditions.checkNotNull(typeToken, "typeToken");
        this.typeToken = typeToken;
        this.suppportChildTypes = suppportChildTypes;
    }

    public final TypeToken<T> getTypeToken() {
        return this.typeToken;
    }

    @Override
    public final boolean canDeserialize(TypeToken<?> type) {
        return this.suppportChildTypes ? this.typeToken.isSupertypeOf(type) : this.typeToken.equals(type);
    }

}