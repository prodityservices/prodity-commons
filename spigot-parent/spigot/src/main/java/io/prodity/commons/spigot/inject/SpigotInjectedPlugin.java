package io.prodity.commons.spigot.inject;

import com.google.common.collect.ImmutableSet;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.PluginBridge;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.impl.DefaultPluginBinder;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
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
        this.callEvent(InjectionFeature::preLoad);
        if (!InjectUtils.loadDescriptors(this.getClassLoader(), this.serviceLocator)) {
            this.serviceLocator.shutdown();
            this.serviceLocator = null;
            this.injectionFeatures = Collections.emptyList();
            this.getLogger().severe("Failed to load injection inhabitants file.");
            this.getLogger().severe("Disabling...");
            return;
        }
        this.callEvent(InjectionFeature::postLoad);
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
    public final void onEnable() {
        if (this.serviceLocator != null) {
            this.callEvent(InjectionFeature::preEnable);
            this.callEvent(InjectionFeature::onEnable);
            this.callEvent(InjectionFeature::postEnable);
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void onDisable() {
        if (this.serviceLocator != null) {
            this.callEvent(InjectionFeature::preDisable);
            PluginBridge.unbridge(this);
            this.serviceLocator.shutdown();
            this.serviceLocator = null;
        }
    }

    private void callEvent(BiConsumer<InjectionFeature, ProdityPlugin> function) {
        for (InjectionFeature feature : this.injectionFeatures) {
            try {
                function.accept(feature, this);
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "Exception from InjectionFeature", e);
            }
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