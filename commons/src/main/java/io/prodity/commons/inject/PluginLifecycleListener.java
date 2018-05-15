package io.prodity.commons.inject;

import io.prodity.commons.plugin.ProdityPlugin;
import org.jvnet.hk2.annotations.Contract;

/**
 * Receives lifecycle events about a plugin.  If a PluginLifecycleListener
 * is exported it will receive events for the owning plugin as well as all
 * future plugins.  A PluginLifecycleListener will no longer receive events
 * once the owning plugin has been disabled.
 */
@Contract
public interface PluginLifecycleListener {

    default void onEnable(ProdityPlugin plugin) {
    }

    default void onDisable(ProdityPlugin plugin) {
    }

}