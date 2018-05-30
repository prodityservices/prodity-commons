package io.prodity.commons.inject;

import io.prodity.commons.inject.bind.BindingBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import org.glassfish.hk2.api.DescriptorVisibility;

/**
 * Exported services are visible to other plugins.  To provide explicit bindings
 * that export a service, use {@link
 * BindingBuilder#withVisibility(DescriptorVisibility)}
 * and add the metadata pair ({@link #PLUGIN_META_KEY}, "YourPluginName").
 * Contracts do not need to be exported.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Export {

    String PLUGIN_META_KEY = "PLUGIN";

}
