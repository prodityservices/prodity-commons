package io.prodity.commons.inject;

import io.prodity.commons.plugin.ProdityPlugin;
import org.jvnet.hk2.annotations.Contract;

/**
 * Receives lifecycle events about the plugin that provided it.
 * PluginLifecycleListeners only receive events for a single plugin.
 * To receive events about all plugins, listen to the platform-specific
 * plugin initialization events.
 */
@Contract
public interface PluginLifecycleListener {

    default void onEnable(ProdityPlugin plugin) {
    }

    default void onDisable(ProdityPlugin plugin) {
    }

}