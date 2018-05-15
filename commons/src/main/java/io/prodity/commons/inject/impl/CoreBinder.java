package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.bind.BindingBuilder;
import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.DescriptorVisibility;

/**
 * Abstract parent class for Spigot commons and Bungee commons.  Implementations
 * should ensure that there is an implementation of {@link Platform} available
 * to handle platform-specific operations.
 */
public abstract class CoreBinder extends PluginBinder {

    protected CoreBinder(ProdityPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void configure() {
        this.addCoreFeatures(ExportFeature.class, ListenerFeature.class, SoftDependFeature.class);
    }

    @SafeVarargs
    protected final void addCoreFeatures(Class<? extends InjectionFeature>...classes) {
        for (Class<?> clazz : classes) {
            this.export(clazz).to(InjectionFeature.class);
        }
    }


}
