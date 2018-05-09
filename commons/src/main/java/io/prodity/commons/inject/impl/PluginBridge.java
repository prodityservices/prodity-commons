package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.extras.ExtrasUtilities;

public class PluginBridge {

    private static List<ProdityPlugin> links = new LinkedList<>();

    public static void bridge(ProdityPlugin plugin) {
        if (links.isEmpty()) {
            links.add(plugin);
        } else {
            ProdityPlugin tail = links.get(links.size() - 1);
            links.add(plugin);
            bidirectionalBridge(tail.getPluginRoot(), plugin.getPluginRoot());
        }
    }

    public static void unbridge(ProdityPlugin plugin) {
        int index = links.indexOf(plugin);
        if (index != -1) {
            ProdityPlugin previous = index == 0 ? null : links.get(index - 1);
            ProdityPlugin next = index == links.size() - 1 ? null : links.get(index + 1);
            if (previous != null && next != null) {
                bidirectionalBridge(previous.getPluginRoot(), next.getPluginRoot());
            }
            if (previous != null) {
                removeBidirectional(previous.getPluginRoot(), plugin.getPluginRoot());
            }
            if (next != null) {
                removeBidirectional(next.getPluginRoot(), plugin.getPluginRoot());
            }
            links.remove(index);
        }
    }

    private static void bidirectionalBridge(ServiceLocator one, ServiceLocator two) {
        ExtrasUtilities.bridgeServiceLocator(one, two);
        ExtrasUtilities.bridgeServiceLocator(two, one);
    }

    private static void removeBidirectional(ServiceLocator one, ServiceLocator two) {
        ExtrasUtilities.unbridgeServiceLocator(one, two);
        ExtrasUtilities.unbridgeServiceLocator(two, one);
    }

    public static List<InjectionFeature> findExternalFeaturesFor(ProdityPlugin plugin) {
        if (links.isEmpty()) {
            return Collections.emptyList();
        } else {
            ProdityPlugin head = links.get(0);

            //noinspection unchecked
            return (List<InjectionFeature>) head.getServices().getAllServices(new InjectionFeatureFilter(plugin));
        }
    }
}
