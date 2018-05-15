package io.prodity.commons.inject;

import io.prodity.commons.inject.impl.InjectionFeatureFilter;
import io.prodity.commons.inject.impl.PluginBridge;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;

import java.io.IOException;
import java.util.List;

public class InjectUtils {

    public static Filter filterByPlugin(ProdityPlugin plugin) {
        return d -> InjectUtils.isFromPlugin(d, plugin);
    }

    public static boolean isFromPlugin(Descriptor descriptor, ProdityPlugin plugin) {
        return plugin.getName().equals(InjectUtils.getOwner(descriptor));
    }

    public static String getOwner(Descriptor descriptor) {
        return descriptor.getMetadata().containsKey(Export.PLUGIN_META_KEY) ? descriptor.getMetadata().get(Export.PLUGIN_META_KEY).get(0)
            : null;
    }

    public static boolean loadDescriptors(ClassLoader loader, ServiceLocator into) {
        List<DescriptorProcessor> processors = into.getAllServices(DescriptorProcessor.class);
        DynamicConfigurationService dcs = into.getService(DynamicConfigurationService.class);
        try
        {
            dcs.getPopulator().populate(new ClasspathDescriptorFileFinder(loader), processors.toArray(new DescriptorProcessor[0]));
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<InjectionFeature> findFeaturesFor(ProdityPlugin plugin) {
        //noinspection unchecked
        return (List<InjectionFeature>) plugin.getServices().getAllServices(new InjectionFeatureFilter(plugin));
    }

}