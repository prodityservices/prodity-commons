package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;

public class InjectionContainer {

    private final ProdityPlugin plugin;
    private List<InjectionFeature> injectionFeatures = Collections.emptyList();
    private ServiceLocator serviceLocator;

    public InjectionContainer(ProdityPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(Runnable initializer) {
        this.serviceLocator = InjectUtils.createLocator(this.plugin);
        PluginBridge.bridge(this.plugin);
        initializer.run();
        this.injectionFeatures = InjectUtils.getDependentServices(this.plugin, InjectionFeature.class);
        this.callEvent(InjectionFeature::preLoad);
        if (!this.loadDescriptors()) {
            this.serviceLocator.shutdown();
            this.serviceLocator = null;
            this.injectionFeatures = Collections.emptyList();
            return;
        }
        this.callEvent(InjectionFeature::postLoad);
    }

    public void enable() {
        if (this.serviceLocator != null) {
            this.callEvent(InjectionFeature::preEnable);
            this.callEvent(InjectionFeature::onEnable);
            this.callEvent(InjectionFeature::postEnable);
        }
    }

    public void disable() {
        if (this.serviceLocator != null) {
            this.callEvent(InjectionFeature::preDisable);
            PluginBridge.unbridge(this.plugin);
            this.serviceLocator.shutdown();
            this.serviceLocator = null;
        }
    }

    private boolean loadDescriptors() {
        List<DescriptorProcessor> processors = this.serviceLocator.getAllServices(DescriptorProcessor.class);
        DynamicConfigurationService dcs = this.serviceLocator.getService(DynamicConfigurationService.class);
        try {
            dcs.getPopulator().populate(new ClasspathDescriptorFileFinder(this.plugin.getClass().getClassLoader()),
                processors.toArray(new DescriptorProcessor[0]));
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load Service Descriptors", e);
            this.plugin.getLogger().severe("Disabling...");
            return false;
        }
        return true;
    }

    private void callEvent(BiConsumer<InjectionFeature, ProdityPlugin> function) {
        for (InjectionFeature feature : this.injectionFeatures) {
            try {
                function.accept(feature, this.plugin);
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Exception from InjectionFeature", e);
            }
        }
    }

    public boolean isEnabled() {
        return this.serviceLocator != null;
    }

    public ServiceLocator getServices() {
        return this.serviceLocator;
    }
}
