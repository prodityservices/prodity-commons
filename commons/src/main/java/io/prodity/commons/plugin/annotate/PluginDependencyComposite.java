package io.prodity.commons.plugin.annotate;


import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.prodity.commons.plugin.annotate.process.PluginSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import javax.annotation.Nonnull;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginDependencyComposite {

    PluginDependency[] value();

    class Serializer {

        @Nonnull
        public static PluginSerializer<PluginDependencyComposite> create(@Nonnull String dependsKey, @Nonnull String softDependsKey) {
            Preconditions.checkNotNull(dependsKey, "dependsKey");
            Preconditions.checkNotNull(softDependsKey, "softDependsKey");

            return (annotation, data) -> {
                if (annotation.value().length == 0) {
                    return;
                }

                final Set<String> dependencies = Sets.newHashSet();
                final Set<String> softDependencies = Sets.newHashSet();

                for (PluginDependency dependency : annotation.value()) {
                    if (dependency.soft()) {
                        softDependencies.add(dependency.value());
                    } else {
                        dependencies.add(dependency.value());
                    }
                }

                if (!dependencies.isEmpty()) {
                    data.set(dependsKey, PluginDependencyComposite.Serializer.blockAndJoin(dependencies));
                }

                if (!softDependencies.isEmpty()) {
                    data.set(softDependsKey, PluginDependencyComposite.Serializer.blockAndJoin(softDependencies));
                }
            };
        }

        private static String blockAndJoin(Iterable<String> strings) {
            return "[" + String.join(",", strings) + "]";
        }

    }

}