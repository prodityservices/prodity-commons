package io.prodity.commons.inject;

import org.glassfish.hk2.api.PopulatorPostProcessor;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Contract;

/**
 * Processes Descriptors for a plugin as they're loaded. Plugins
 * cannot provide their own dynamically loaded PluginProcessors without
 * explicit bindings.
 * <p />
 * To provide a DescriptorProcessor for another plugin, see {@link InjectionFeature}.
 * Each plugin's processors are instantiated before the plugin
 * ServiceLocators are bridged, so they must be provided by an
 * InjectionFeature.
 */
@Contract
public interface DescriptorProcessor extends PopulatorPostProcessor {
}
