package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DoNotRegister;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InstanceLifecycleEvent;
import org.glassfish.hk2.api.InstanceLifecycleEventType;
import org.glassfish.hk2.api.InstanceLifecycleListener;

public class ListenerRegistration implements InstanceLifecycleListener, PluginLifecycleListener {

    private final Queue<Object> earlyRegistered = new ConcurrentLinkedQueue<>();
    private final Set<ActiveDescriptor<?>> registered = ConcurrentHashMap.newKeySet();
    private final ProdityPlugin plugin;
    private final Platform platform;

    @Inject
    public ListenerRegistration(ProdityPlugin plugin, Platform platform) {
        this.plugin = plugin;
        this.platform = platform;
    }

    @Override
    public Filter getFilter() {
        return InjectUtils.filterByPlugin(this.plugin);
    }

    @Override
    public void lifecycleEvent(InstanceLifecycleEvent event) {
        if (event.getEventType() == InstanceLifecycleEventType.POST_PRODUCTION) {
            if (this.shouldRegister(event.getLifecycleObject()) && this.registered.add(event.getActiveDescriptor())) {
                if (this.platform.isEnabled()) {
                    this.register(event.getLifecycleObject());
                } else {
                    this.earlyRegistered.add(event.getLifecycleObject());
                }
            }
        } else if (event.getEventType() == InstanceLifecycleEventType.PRE_DESTRUCTION) {
            if (this.registered.remove(event.getActiveDescriptor())) {
                this.unregister(event.getLifecycleObject());
            }
        }
    }

    private boolean shouldRegister(Object candidate) {
        return this.platform.isListener(candidate) && candidate.getClass().getAnnotation(DoNotRegister.class) == null;
    }

    private void register(Object candidate) {
        this.platform.registerListener(candidate);
    }

    private void unregister(Object listener) {
        this.platform.unregisterListener(listener);
    }

    @Override
    public void onEnable(ProdityPlugin plugin) {
        for (Object listener : this.earlyRegistered) {
            this.register(listener);
        }
        this.earlyRegistered.clear();
    }

}