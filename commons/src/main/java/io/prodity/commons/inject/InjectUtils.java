package io.prodity.commons.inject;

import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.Filter;

public class InjectUtils {

    public static Filter filterByPlugin(ProdityPlugin plugin) {
        return d -> isFromPlugin(d, plugin);
    }

    public static boolean isFromPlugin(Descriptor descriptor, ProdityPlugin plugin) {
        return plugin.getName().equals(getOwner(descriptor));
    }

    public static String getOwner(Descriptor descriptor) {
        return descriptor.getMetadata().containsKey(Export.PLUGIN_META_KEY) ? descriptor.getMetadata().get(Export.PLUGIN_META_KEY).get(0)
            : null;
    }
}
