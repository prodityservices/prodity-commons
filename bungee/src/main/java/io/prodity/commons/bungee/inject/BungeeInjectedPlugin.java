package io.prodity.commons.bungee.inject;

import io.prodity.commons.bungee.inject.impl.DefaultPluginBinder;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.InjectionContainer;
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

    private final InjectionContainer container = new InjectionContainer(this);

    @Override
    public void onLoad() {
        this.container.load(this::initialize);
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
    }

    @Override
    public void onEnable() {
        this.container.enable();
    }

    @Override
    public void onDisable() {
        this.container.disable();
    }


    @Nonnull
    @Override
    public Logger getLogger() {
        return super.getLogger();
    }

    @Override
    public ServiceLocator getServices() {
        return this.container.getServices();
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
