package io.prodity.commons.config.load.value;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.except.ConfigLoadException;
import io.prodity.commons.config.load.ConfigLoadContext;
import io.prodity.commons.except.tryto.Try;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;

public abstract class ConfigDeserializer<T> {

    private final Class<T> typeClass;

    public ConfigDeserializer(@Nonnull Class<T> typeClass) {
        Preconditions.checkNotNull(typeClass, "typeClass");
        this.typeClass = typeClass;
    }

    @Nonnull
    public Class<T> getTypeClass() {
        return this.typeClass;
    }

    abstract T deserialize(ConfigurationNode node);

    public T deserialize(@Nonnull ConfigLoadContext<?> context, @Nonnull ConfigurationNode node) throws ConfigLoadException {
        Preconditions.checkNotNull(context, "context");
        Preconditions.checkNotNull(node, "node");
        return Try.mapExceptionTo(() -> this.deserialize(node), ConfigLoadException.newMapper(context)).get();
    }

}