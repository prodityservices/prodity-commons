package io.prodity.commons.spigot.plugin.annotate;

import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(PluginLoadBeforeComposite.class)
public @interface PluginLoadBefore {

    String value();

    class Serializer {

        public static final String KEY = "loadbefore";

        public static PluginSerializer<PluginLoadBefore> create() {
            return (annotation, data) -> {
                final String pluginName = annotation.value();
                data.set(PluginLoadBefore.Serializer.KEY, pluginName);
            };
        }

    }

}