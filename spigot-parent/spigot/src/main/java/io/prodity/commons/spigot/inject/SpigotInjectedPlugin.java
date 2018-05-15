package io.prodity.commons.spigot.inject;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.Eager;
import io.prodity.commons.inject.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.PluginLifecycleListener;
import io.prodity.commons.inject.impl.PluginBridge;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.impl.DefaultPluginBinder;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

/**
 * InjectedPlugin should be extended by plugins wishing to utilize automatic dependency injection.
 * Initialization and cleanup logic should be performed by annotating methods with {@link javax.annotation.PostConstruct}
 * and {@link javax.annotation.PreDestroy}. InjectedPlugin implements Listener and is automatically registered.
 */
public class SpigotInjectedPlugin extends JavaPlugin implements Listener, ProdityPlugin {
    private Set<String> softDependencies;
    private Set<String> dependencies;
    private ServiceLocator serviceLocator;
    private List<InjectionFeature> injectionFeatures = Collections.emptyList();

    @Override
    public final void onLoad() {
        this.softDependencies = ImmutableSet.copyOf(this.getDescription().getSoftDepend());
        this.dependencies = ImmutableSet.copyOf(this.getDescription().getDepend());
        this.serviceLocator = ServiceLocatorFactory.getInstance().create(this.getName());
        this.initialize();
        this.injectionFeatures = InjectUtils.findFeaturesFor(this);
        this.injectionFeatures.forEach(f -> f.preLoad(this));
        if (!InjectUtils.loadDescriptors(this.getClassLoader(), this.serviceLocator)) {
            this.getLogger().severe("Failed to load injection inhabitants file.");
            this.getLogger().severe("Disabling...");
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
     *
     */
    protected void initialize() {
        ServiceLocatorUtilities.bind(this.getServices(), new DefaultPluginBinder(this));
        PluginBridge.bridge(this);
    }

    @Override
    public final void onEnable() {
        if (this.serviceLocator != null) {
            this.injectionFeatures.forEach(f -> f.preEnable(this));
            this.serviceLocator.inject(this);
            this.serviceLocator.postConstruct(this);
            this.serviceLocator.getAllServices(Eager.class);
            this.serviceLocator.getAllServices(PluginLifecycleListener.class).forEach(l -> l.onEnable(this));
            this.injectionFeatures.forEach(f -> f.postEnable(this));
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void onDisable() {
        if (this.serviceLocator != null) {
            this.injectionFeatures.forEach(f -> f.preDisable(this));
            this.serviceLocator.getAllServices(PluginLifecycleListener.class).forEach(l -> l.onDisable(this));
            this.serviceLocator.preDestroy(this);
            PluginBridge.unbridge(this);
            this.serviceLocator.shutdown();
            this.serviceLocator = null;
        }
    }

    /**
     * Gets the ServiceLocator used by this plugin.  Returns null
     * if this plugin is disabled.
     *
     * @return nullable ServiceLocator
     */
    @Override
    public final ServiceLocator getServices() {
        return this.serviceLocator;
    }

    @Override
    public Set<String> getDependencies() {
        return this.dependencies;
    }

    @Override
    public Set<String> getSoftDependencies() {
        return this.softDependencies;
    }

}