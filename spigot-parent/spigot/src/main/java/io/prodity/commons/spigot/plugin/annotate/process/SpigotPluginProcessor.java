package io.prodity.commons.spigot.plugin.annotate.process;

import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.plugin.annotate.PluginDependencyComposite;
import io.prodity.commons.plugin.annotate.process.PluginProcessor;
import io.prodity.commons.plugin.annotate.process.PluginSerializerMap;
import io.prodity.commons.spigot.plugin.annotate.PluginAuthor;
import io.prodity.commons.spigot.plugin.annotate.PluginAuthorComposite;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadBefore;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadBeforeComposite;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadState;
import io.prodity.commons.spigot.plugin.annotate.PluginWebsite;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.prodity.commons.plugin.annotate.Plugin"})
public class SpigotPluginProcessor extends PluginProcessor {

    public static final String DEPEND_KEY = "depend";
    public static final String SOFT_DEPEND_KEY = "softdepend";
    public static final String PROPERTIES_FILE = "app.properties";

    private static final PluginSerializerMap SERIALIZER_MAP = PluginSerializerMap.builder()
        .put(Plugin.class, Plugin.Serializer.create())
        .put(PluginDependencyComposite.class,
            PluginDependencyComposite.Serializer.create(SpigotPluginProcessor.DEPEND_KEY, SpigotPluginProcessor.SOFT_DEPEND_KEY))
        .put(PluginDependency.class,
            PluginDependency.Serializer.create(SpigotPluginProcessor.DEPEND_KEY, SpigotPluginProcessor.SOFT_DEPEND_KEY))
        .put(PluginAuthor.class, PluginAuthor.Serializer.create())
        .put(PluginAuthorComposite.class, PluginAuthorComposite.Serializer.create())
        .put(PluginLoadState.class, PluginLoadState.Serializer.create())
        .put(PluginWebsite.class, PluginWebsite.Serializer.create())
        .put(PluginLoadBefore.class, PluginLoadBefore.Serializer.create())
        .put(PluginLoadBeforeComposite.class, PluginLoadBeforeComposite.Serializer.create())
        .build();

    public SpigotPluginProcessor() {
        super(SpigotPluginProcessor.SERIALIZER_MAP, SpigotPluginProcessor.PROPERTIES_FILE);
    }

}