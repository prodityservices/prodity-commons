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
public @interface PluginAuthorComposite {

    PluginAuthor[] value();

    class Serializer {

        public static final String KEY = "authors";

        public static PluginSerializer<PluginAuthorComposite> create() {
            return (annotation, data) -> {
                final PluginAuthor[] authors = annotation.value();

                if (authors.length == 0) {
                    return;
                }

                if (authors.length == 1) {
                    data.set(PluginAuthor.Serializer.KEY, Lists.newArrayList(authors[0].value()));
                    return;
                }

                final List<String> authorNames = Arrays.stream(authors)
                    .map(PluginAuthor::value)
                    .collect(Collectors.toList());

                data.set(PluginAuthorComposite.Serializer.KEY, authorNames);
            };
        }

    }

}