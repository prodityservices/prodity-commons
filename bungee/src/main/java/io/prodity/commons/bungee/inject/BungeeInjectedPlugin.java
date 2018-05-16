package io.prodity.commons.bungee.inject;

import io.prodity.commons.bungee.inject.impl.DefaultPluginBinder;
import io.prodity.commons.inject.Eager;
import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.inject.impl.PluginBridge;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

public abstract class BungeeInjectedPlugin extends Plugin implements ProdityPlugin, Listener {

    private ServiceLocator serviceLocator;

    // Initialize and save these so that any implementation that gets one event
    // gets all of them.
    private List<InjectionFeature> injectionFeatures = Collections.emptyList();

    @Override
    public void onLoad() {
        this.serviceLocator = ServiceLocatorFactory.getInstance().create(this.getName());
        this.initialize();
        this.injectionFeatures = InjectUtils.findFeaturesFor(this);
        this.injectionFeatures.forEach(f -> f.preLoad(this));
        if (!InjectUtils.loadDescriptors(this.getClass().getClassLoader(), this.serviceLocator)) {
            return;
        }
        this.injectionFeatures.forEach(f -> f.postLoad(this));
    }

    /**
     * Install any plugin-specific binders, or modify the ServiceLocator at will.
     * Keep in mind that anything bound with {@link org.glassfish.hk2.api.DescriptorVisibility#NORMAL}, the default
     * visibility, is going to be visible to all other plugins.  It's suggested to
     * use {@link io.prodity.commons.inject.bind.PluginBinder} to provide
     * custom bindings, as this will automatically handle visibility.  If you
     * must use a regular binder, it's suggested to annotate your service with
     * {@link org.glassfish.hk2.api.Visibility} to ensure it's not available to
     * all plugins.
     */
    protected void initialize() {
        ServiceLocatorUtilities.bind(this.getServices(), new DefaultPluginBinder(this));
        PluginBridge.bridge(this);
    }

    @Override
    public void onEnable() {
        this.injectionFeatures.forEach(f -> f.preEnable(this));
        this.serviceLocator.inject(this);
        this.serviceLocator.postConstruct(this);
        this.serviceLocator.getAllServices(Eager.class);
        this.serviceLocator.getAllServices(PluginLifecycleListener.class).forEach(l -> l.onEnable(this));
        this.injectionFeatures.forEach(f -> f.postEnable(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
