package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InstanceLifecycleEvent;
import org.glassfish.hk2.api.InstanceLifecycleEventType;
import org.glassfish.hk2.api.InstanceLifecycleListener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListenerRegistration implements InstanceLifecycleListener, PluginLifecycleListener {
	private final Set<ActiveDescriptor<?>> registered = new HashSet<>();
	private final List<Listener> initializedBeforePlugin = new ArrayList<>();
	private final SpigotInjectedPlugin plugin;
	private final ListenerFilter filter;

	@Inject
	public ListenerRegistration(SpigotInjectedPlugin plugin, ListenerFilter filter) {
		this.plugin = plugin;
		this.filter = filter;
	}

	public Filter getFilter() {
		return InjectUtils.filterByPlugin(plugin);
	}

	@Override
	public void lifecycleEvent(InstanceLifecycleEvent event) {
		Object potentialListener = event.getLifecycleObject();
		if (event.getEventType() == InstanceLifecycleEventType.POST_PRODUCTION) {
			if (potentialListener instanceof Listener && filter.shouldRegister(potentialListener)) {
				if (registered.add(event.getActiveDescriptor())) {
					Listener listener = (Listener) event.getLifecycleObject();
					if (plugin.isEnabled()) {
						register(listener);
					} else {
						initializedBeforePlugin.add(listener);
					}
				}
			}
		} else if (event.getEventType() == InstanceLifecycleEventType.PRE_DESTRUCTION) {
			if (registered.remove(event.getActiveDescriptor())) {
				HandlerList.unregisterAll((Listener) event.getLifecycleObject());
			}
		}
	}

	private void register(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	@Override
	public void onEnable(ProdityPlugin plugin) {
		initializedBeforePlugin.forEach(this::register);
		initializedBeforePlugin.clear();
	}

	@Override
	public void onDisable(ProdityPlugin plugin) {
		// Let bukkit unregister them
		registered.clear();
	}
}
