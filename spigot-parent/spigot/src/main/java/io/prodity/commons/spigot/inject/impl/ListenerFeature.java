package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class ListenerFeature implements InjectionFeature
{
	@Override
	public void postLoad(ProdityPlugin plugin)
	{
	    bind(plugin, binder -> {
            binder.bind(DefaultFilter.class).to(ListenerFilter.class).ranked(-1);
            binder.bind(ListenerRegistration.class)
					.to(InstanceLifecycleListener.class)
					.to(PluginLifecycleListener.class);
        });
	}
}
