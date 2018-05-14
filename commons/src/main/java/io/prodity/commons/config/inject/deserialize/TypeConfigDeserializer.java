package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.config.inject.element.ConfigElement;
import java.util.Objects;

/**
 * {@link ConfigDeserializer} implementation for an explicit {@link TypeToken}.
 *
 * @param <T> the type to be deserialized
 */
public abstract class TypeConfigDeserializer<T> implements ConfigDeserializer<T> {

    private final TypeToken<T> typeToken;

    public TypeConfigDeserializer(TypeToken<T> typeToken) {
        Preconditions.checkNotNull(typeToken, "typeToken");
        this.typeToken = typeToken;
    }

    public final TypeToken<T> getTypeToken() {
        return this.typeToken;
    }

    @Override
    public final boolean canDeserialize(ConfigElement<T> element) {
        return Objects.equals(element.getType(), this.typeToken);
    }

}