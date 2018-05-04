package io.prodity.commons.spigot.plugin.annotate;

import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginLoadState {

    LoadState value();

    enum LoadState {

        STARTUP,
        POSTWORLD

    }

    class Serializer {

        public static final String KEY = "load";

        @Nonnull
        public static PluginSerializer<PluginLoadState> create() {
            return (annotation, data) -> {
                final LoadState loadState = annotation.value();
                data.set(Serializer.KEY, loadState.name());
            };
        }

    }

}