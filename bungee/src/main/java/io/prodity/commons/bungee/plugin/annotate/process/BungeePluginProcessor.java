package io.prodity.commons.bungee.plugin.annotate.process;

import io.prodity.commons.bungee.plugin.annotate.PluginAuthor;
import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.plugin.annotate.PluginDependencyComposite;
import io.prodity.commons.plugin.annotate.process.PluginProcessor;
import io.prodity.commons.plugin.annotate.process.PluginSerializerMap;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.prodity.commons.plugin.annotate.Plugin"})
public class BungeePluginProcessor extends PluginProcessor {

    public static final String DEPEND_KEY = "depends";
    public static final String SOFT_DEPEND_KEY = "softDepends";
    public static final String PROPERTIES_FILE = "app.properties";

    private static final PluginSerializerMap SERIALIZER_MAP = PluginSerializerMap.builder()
        .put(Plugin.class, Plugin.Serializer.create())
        .put(PluginDependencyComposite.class,
            PluginDependencyComposite.Serializer.create(BungeePluginProcessor.DEPEND_KEY, BungeePluginProcessor.SOFT_DEPEND_KEY))
        .put(PluginDependency.class,
            PluginDependency.Serializer.create(BungeePluginProcessor.DEPEND_KEY, BungeePluginProcessor.SOFT_DEPEND_KEY))
        .put(PluginAuthor.class, PluginAuthor.Serializer.create())
        .build();

    public BungeePluginProcessor() {
        super(BungeePluginProcessor.SERIALIZER_MAP, BungeePluginProcessor.PROPERTIES_FILE);
    }

}