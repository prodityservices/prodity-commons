package io.prodity.commons.spigot.plugin.annotate;

import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginWebsite {

    String value();

    class Serializer {

        public static final String KEY = "website";

        public static PluginSerializer<PluginWebsite> create() {
            return (annotation, data) -> {
                final String website = annotation.value();
                data.set(PluginWebsite.Serializer.KEY, website);
            };
        }

    }

}