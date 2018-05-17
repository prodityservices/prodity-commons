package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;

public enum InjectUtils {

    ;

    public static Filter filterByPlugin(ProdityPlugin plugin) {
        return d -> InjectUtils.isFromPlugin(d, plugin);
    }

    public static boolean isFromPlugin(Descriptor descriptor, ProdityPlugin plugin) {
        return plugin.getName().equals(InjectUtils.getOwner(descriptor));
    }

    @Nullable
    public static String getOwner(Descriptor descriptor) {
        return descriptor.getMetadata().containsKey(Export.PLUGIN_META_KEY) ? descriptor.getMetadata().get(Export.PLUGIN_META_KEY).get(0)
            : null;
    }

    public static boolean loadDescriptors(ClassLoader loader, ServiceLocator into) {
        List<DescriptorProcessor> processors = into.getAllServices(DescriptorProcessor.class);
        DynamicConfigurationService dcs = into.getService(DynamicConfigurationService.class);
        try {
            dcs.getPopulator().populate(new ClasspathDescriptorFileFinder(loader), processors.toArray(new DescriptorProcessor[0]));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static <T> List<T> getLocalServices(ProdityPlugin plugin, Class<T> tClass) {
        //noinspection unchecked
        return (List<T>) plugin.getServices().getAllServices(new LocalServiceFilter<>(plugin, tClass));
    }

    public static List<InjectionFeature> findFeaturesFor(ProdityPlugin plugin) {
        //noinspection unchecked
        return (List<InjectionFeature>) plugin.getServices().getAllServices(new InjectionFeatureFilter(plugin));
    }

}