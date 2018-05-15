package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class ListenerFeature implements InjectionFeature {

	@Override
	public void preLoad(ProdityPlugin plugin) {
	    this.bind(plugin, binder -> {
            binder.bind(ListenerRegistration.class)
                .to(InstanceLifecycleListener.class)
                .to(PluginLifecycleListener.class);
        });
    }
}
