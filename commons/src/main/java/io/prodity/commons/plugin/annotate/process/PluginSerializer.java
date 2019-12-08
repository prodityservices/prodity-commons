package io.prodity.commons.plugin.annotate.process;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface PluginSerializer<T extends Annotation> {

    void serializeIntoDescription(T annotation, PluginDescription data);

}