package io.prodity.commons.bungee.plugin.annotate;

import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginAuthor {

    String value();

    class Serializer {

        public static final String KEY = "author";

        @Nonnull
        public static PluginSerializer<PluginAuthor> create() {
            return (annotation, data) -> {
                final String authorName = annotation.value();
                data.set(PluginAuthor.Serializer.KEY, authorName);
            };
        }

    }

}