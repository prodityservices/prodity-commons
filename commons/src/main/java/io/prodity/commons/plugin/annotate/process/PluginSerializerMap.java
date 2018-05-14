package io.prodity.commons.plugin.annotate.process;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;

public class PluginSerializerMap {

    public static class Builder {

        private final ImmutableMap.Builder<Class<? extends Annotation>, PluginSerializer<?>> mapBuilder;

        private Builder() {
            this.mapBuilder = ImmutableMap.builder();
        }

        public <T extends Annotation> PluginSerializerMap.Builder put(Class<T> annotation,
            PluginSerializer<T> serializer) {
            Preconditions.checkNotNull(annotation, "annotation");
            Preconditions.checkNotNull(serializer, "serializer");
            this.mapBuilder.put(annotation, serializer);
            return this;
        }

        public PluginSerializerMap build() {
            return new PluginSerializerMap(this.mapBuilder.build());
        }

    }

    public static PluginSerializerMap.Builder builder() {
        return new PluginSerializerMap.Builder();
    }

    private final ImmutableMap<Class<? extends Annotation>, PluginSerializer<?>> serializers;

    private PluginSerializerMap(ImmutableMap<Class<? extends Annotation>, PluginSerializer<?>> serializers) {
        Preconditions.checkNotNull(serializers, "serializers");
        this.serializers = serializers;
    }

    public void forEach(BiConsumer<Class<? extends Annotation>, PluginSerializer<?>> biConsumer) {
        Preconditions.checkNotNull(biConsumer, "biConsumer");
        this.serializers.forEach(biConsumer);
    }

    @Nullable
    public <T extends Annotation> PluginSerializer<T> getSerializer(Class<T> annotationClass) {
        Preconditions.checkNotNull(annotationClass, "annotationClass");
        final PluginSerializer<T> serializer = (PluginSerializer<T>) this.serializers.get(annotationClass);
        return serializer;
    }

}