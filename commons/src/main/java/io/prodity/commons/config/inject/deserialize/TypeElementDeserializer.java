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