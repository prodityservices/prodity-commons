package io.prodity.commons.spigot.legacy;

import io.prodity.commons.plugin.ProdityPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a {@link ProdityPlugin} implementation to declare that the {@link LegacyFeature} should inject all legacy
 * assets into the plugin.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLegacy {
}