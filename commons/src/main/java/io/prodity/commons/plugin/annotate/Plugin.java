package io.prodity.commons.plugin.annotate;

import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Plugin {

    String name();

    String version();

    String description();

    class Serializer {

        public static final String NAME_KEY = "name";
        public static final String VERSION_KEY = "version";
        public static final String DESCRIPTION_KEY = "description";

        @Nonnull
        public static PluginSerializer<Plugin> create() {
            return (annotation, data) -> {
                data.set(Plugin.Serializer.NAME_KEY, annotation.name());
                data.set(Plugin.Serializer.VERSION_KEY, annotation.version());
                data.set(Plugin.Serializer.DESCRIPTION_KEY, annotation.description());
            };
        }

    }

}