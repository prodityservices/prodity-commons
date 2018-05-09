package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InstanceLifecycleEvent;
import org.glassfish.hk2.api.InstanceLifecycleEventType;
import org.glassfish.hk2.api.InstanceLifecycleListener;

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

    @Override
    public Filter getFilter() {
        return InjectUtils.filterByPlugin(this.plugin);
    }

    @Override
    public void lifecycleEvent(InstanceLifecycleEvent event) {
        Object potentialListener = event.getLifecycleObject();
        if (event.getEventType() == InstanceLifecycleEventType.POST_PRODUCTION) {
            if (potentialListener instanceof Listener && this.filter.shouldRegister(potentialListener)) {
                if (this.registered.add(event.getActiveDescriptor())) {
                    Listener listener = (Listener) event.getLifecycleObject();
                    if (this.plugin.isEnabled()) {
                        this.register(listener);
                    } else {
                        this.initializedBeforePlugin.add(listener);
                    }
                }
            }
        } else if (event.getEventType() == InstanceLifecycleEventType.PRE_DESTRUCTION) {
            if (this.registered.remove(event.getActiveDescriptor())) {
                HandlerList.unregisterAll((Listener) event.getLifecycleObject());
            }
        }
    }

    private void register(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    @Override
    public void onEnable(ProdityPlugin plugin) {
        this.initializedBeforePlugin.forEach(this::register);
        this.initializedBeforePlugin.clear();
    }

    @Override
    public void onDisable(ProdityPlugin plugin) {
        // Let bukkit unregister them
        this.registered.clear();
    }
}
