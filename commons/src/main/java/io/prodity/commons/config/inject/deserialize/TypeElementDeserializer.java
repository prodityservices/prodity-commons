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

    public TypeElementDeserializer(TypeToken<T> typeToken, int priority) {
        super(priority);
        Preconditions.checkNotNull(typeToken, "typeToken");
        this.typeToken = typeToken;
    }

    public final TypeToken<T> getTypeToken() {
        return this.typeToken;
    }

    @Override
    public boolean canDeserialize(TypeToken<?> type) {
        return this.typeToken.isSupertypeOf(type);
    }

}