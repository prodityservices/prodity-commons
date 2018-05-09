package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.ExportFeature;
import io.prodity.commons.spigot.ProdityCommonsSpigot;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.bind.PluginBinder;
import org.glassfish.hk2.api.DescriptorVisibility;

/**
 * This is the PluginBinder that Commons uses to export all the default features
 */
public class InternalBinder extends PluginBinder {
    public InternalBinder(ProdityCommonsSpigot pluginInject) {
        super(pluginInject);
    }

    @Override
    protected void configure() {
        addCoreFeatures(ListenerFeature.class, ExportFeature.class, SoftDependFeature.class,
                McVersionFeature.class, TaskFeature.class);
    }


    @SafeVarargs
    private final void addCoreFeatures(Class<? extends InjectionFeature>...classes) {
        for (Class<?> clazz : classes) {
            bind(clazz).to(InjectionFeature.class)
                    .withVisibility(DescriptorVisibility.NORMAL);
        }
    }
}
