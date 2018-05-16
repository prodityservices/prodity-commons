package io.prodity.commons.config.inject.deserialize;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.prodity.commons.color.Color;
import io.prodity.commons.config.annotate.inject.ConfigPath;
import io.prodity.commons.config.inject.deserialize.ElementDeserializers.NumberSerializer;
import io.prodity.commons.repository.registry.RepositoryRegistry;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class ElementDeserializerRegistry {

    private final Set<ElementDeserializer<?>> deserializers = new ConcurrentSkipListSet<>(Comparator.reverseOrder());

    @Inject
    private RepositoryRegistry repositoryRegistry;
    @ConfigPath("")
    private Color color;

    @PostConstruct
    private void registerDefaults() {
        this.register(new NumberSerializer());
    }

    public ElementDeserializerRegistry registerAll(ElementDeserializerRegistry registry) {
        Preconditions.checkNotNull(registry, "registry");
        this.deserializers.addAll(registry.deserializers);
        return this;
    }

    public ElementDeserializerRegistry register(ElementDeserializer<?> deserializer) {
        Preconditions.checkNotNull(deserializer, "deserializer");
        this.deserializers.add(deserializer);
        return this;
    }

    public Optional<ElementDeserializer<?>> get(@Nullable TypeToken<?> type) {
        if (type == null) {
            return Optional.empty();
        }

        for (ElementDeserializer<?> deserializer : this.deserializers) {
            if (deserializer.canDeserialize(type)) {
                return Optional.of(deserializer);
            }
        }

        return Optional.empty();
    }

//    public <T> ElementDeserializerRegistry map(TypeToken<T> type, TypeToken<T> to) throws IllegalStateException {
//        // .map(Color.class).to(ImmutableColor.class) - maps Color fields to ImmutableColor fields
//
//    }

}