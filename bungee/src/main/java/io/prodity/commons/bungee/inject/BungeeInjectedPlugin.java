package io.prodity.commons.bungee.inject;

import io.prodity.commons.bungee.inject.impl.DefaultPluginBinder;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.PluginBridge;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;


import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BungeeInjectedPlugin extends Plugin implements ProdityPlugin, Listener {

    private ServiceLocator serviceLocator;
    private List<InjectionFeature> injectionFeatures = Collections.emptyList();

    @Override
    public void onLoad() {
        this.serviceLocator = ServiceLocatorFactory.getInstance().create(this.getName());
        this.initialize();
        this.injectionFeatures = InjectUtils.findFeaturesFor(this);
        this.callEvent(InjectionFeature::preLoad);
        if (!InjectUtils.loadDescriptors(this.getClass().getClassLoader(), this.serviceLocator)) {
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
    public void onEnable() {
        if (this.serviceLocator != null) {
            this.callEvent(InjectionFeature::preEnable);
            this.callEvent(InjectionFeature::onEnable);
            this.callEvent(InjectionFeature::postEnable);
        }
    }

    @Override
    public void onDisable() {
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

    @Nonnull
    @Override
    public Logger getLogger() {
        return super.getLogger();
    }

    @Override
    public ServiceLocator getServices() {
        return this.serviceLocator;
    }

    @Override
    public String getName() {
        return this.getDescription().getName();
    }

    @Override
    public Set<String> getDependencies() {
        return this.getDescription().getDepends();
    }

    @Override
    public Set<String> getSoftDependencies() {
        return this.getDescription().getSoftDepends();
    }

    @Override
    public InputStream getResource(String fileName) {
        return this.getResourceAsStream(fileName);
    }
}
