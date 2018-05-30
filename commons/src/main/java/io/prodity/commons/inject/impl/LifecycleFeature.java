package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.Eager;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class LifecycleFeature implements InjectionFeature {

    @Override
    public void onEnable(ProdityPlugin plugin) {
        plugin.getServices().inject(plugin);
        plugin.getServices().postConstruct(plugin);
        InjectUtils.getLocalServices(plugin, Eager.class);
        this.callEvent(plugin, PluginLifecycleListener::onEnable);

    }

    @Override
    public void preDisable(ProdityPlugin plugin) {
        this.callEvent(plugin, PluginLifecycleListener::onDisable);
        plugin.getServices().preDestroy(plugin);

    }

    private void callEvent(ProdityPlugin plugin, BiConsumer<PluginLifecycleListener, ProdityPlugin> method) {
        for (PluginLifecycleListener listener : InjectUtils.getLocalServices(plugin, PluginLifecycleListener.class)) {
            try {
                method.accept(listener, plugin);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Exception from PluginLifecycleListener", e);
            }
        }
    }
}
