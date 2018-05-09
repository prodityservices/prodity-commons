package io.prodity.commons.spigot.inject;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.Eager;
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
    // The pluginRoot is bridged with all other ServiceLocators.
    // May be the same as serviceLocator, or may be a parent.  The plugin
    // is in full control of the serviceLocator.
    private ServiceLocator pluginRoot;
    // Initialize and save these so that any implementation that gets one event
    // gets all of them.
    private List<InjectionFeature> injectionFeatures = Collections.emptyList();

    @Override
    public final void onLoad() {
        this.softDependencies = ImmutableSet.copyOf(this.getDescription().getSoftDepend());
        this.dependencies = ImmutableSet.copyOf(this.getDescription().getDepend());
        this.pluginRoot = ServiceLocatorFactory.getInstance().create(this.getName());
        this.serviceLocator = this.initialize(this.pluginRoot);
        this.injectionFeatures = this.findFeatures();
        this.injectionFeatures.forEach(f -> f.preLoad(this));
        if (!this.loadDescriptors()) {
            return;
        }
        this.injectionFeatures.forEach(f -> f.postLoad(this));
        PluginBridge.bridge(this);
    }

    private List<InjectionFeature> findFeatures() {
        List<InjectionFeature> features = this.serviceLocator.getAllServices(InjectionFeature.class);
        features.addAll(PluginBridge.findExternalFeaturesFor(this));
        return features;
    }

    private boolean loadDescriptors() {
        List<DescriptorProcessor> processors = this.serviceLocator.getAllServices(DescriptorProcessor.class);
        DynamicConfigurationService dcs = this.serviceLocator.getService(DynamicConfigurationService.class);
        try {
            dcs.getPopulator()
                .populate(new ClasspathDescriptorFileFinder(this.getClass().getClassLoader()),
                    processors.toArray(new DescriptorProcessor[0]));
        } catch (IOException e) {
            e.printStackTrace();
            this.getLogger().severe("Failed to load injection inhabitants file.");
            this.getLogger().severe("Disabling...");
            return false;
        }
        return true;
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
     * <p />
     * The {@code locator} passed as a parameter to this method will be bridge with all
     * other ServiceLocators in order to support service exporting, regardless of what
     * this method returns.  Plugins that wish to use more advanced HK2 features without
     * worrying about visibility should create a new ServiceLocator using {@link
     * ServiceLocatorFactory#create(String, ServiceLocator)} with the {@code locator} as
     * the parent.  This new service locator should be returned, and will be used to
     * create all discovered services of this plugin.  The plugin will be unable to
     * export any services without explicitly binding it to the {@code locator}, but
     * will not leak services to other plugins in a potentially harmful way. The plugin
     * will still be able to access all services exported by other plugins.
     *
     * @param locator this plugin's ServiceLocator
     * @return the ServiceLocator to populate with discovered services
     */
    protected ServiceLocator initialize(ServiceLocator locator) {
        ServiceLocatorUtilities.bind(locator, DefaultPluginBinder.createFor(this));
        return locator;
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
            this.pluginRoot = null;
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

    /**
     * Gets the ServiceLocator that provides exported members and is
     * bridged with all other plugins.  Either is or is a parent of
     * {@link #getServices()}.  Generally if you're attempting to query
     * the services of a plugin you want to use {@code getServices()}.
     * Returns null if the plugin is disabled.
     *
     * @return nullable ServiceLocator
     */
    @Override
    public final ServiceLocator getPluginRoot() {
        return this.pluginRoot;
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