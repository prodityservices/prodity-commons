package io.prodity.commons.spigot.plugin.annotate;

import com.google.common.collect.Lists;
import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginLoadBeforeComposite {

    class Serializer {

        public static PluginSerializer<PluginLoadBeforeComposite> create() {
            return (annotation, data) -> {
                final PluginLoadBefore[] plugins = annotation.value();

                if (plugins.length == 0) {
                    return;
                }

                if (plugins.length == 1) {
                    data.set(PluginLoadBefore.Serializer.KEY, Lists.newArrayList(plugins[0].value()));
                    return;
                }

                final List<String> pluginNames = Arrays.stream(plugins)
                    .map(PluginLoadBefore::value)
                    .collect(Collectors.toList());

                data.set(PluginLoadBefore.Serializer.KEY, pluginNames);
            };
        }

    }

    PluginLoadBefore[] value();

}