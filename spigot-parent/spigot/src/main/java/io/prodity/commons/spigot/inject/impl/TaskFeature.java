package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class TaskFeature implements InjectionFeature {
    @Override
    public void preLoad(ProdityPlugin plugin) {
        bind(plugin, binder -> {
            binder.bind(TaskRegistration.class)
                    .to(PluginLifecycleListener.class)
                    .to(InstanceLifecycleListener.class);
        });
    }
}
