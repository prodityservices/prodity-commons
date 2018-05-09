package io.prodity.commons.inject.impl;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.extras.ExtrasUtilities;

public class PluginBridge {

    private static final List<ProdityPlugin> links = new LinkedList<>();

    public static void bridge(ProdityPlugin plugin) {
        if (PluginBridge.links.isEmpty()) {
            PluginBridge.links.add(plugin);
        } else {
            ProdityPlugin tail = PluginBridge.links.get(PluginBridge.links.size() - 1);
            PluginBridge.links.add(plugin);
            PluginBridge.bidirectionalBridge(tail.getPluginRoot(), plugin.getPluginRoot());
        }
    }

    public static void unbridge(ProdityPlugin plugin) {
        int index = PluginBridge.links.indexOf(plugin);
        if (index != -1) {
            ProdityPlugin previous = index == 0 ? null : PluginBridge.links.get(index - 1);
            ProdityPlugin next = index == PluginBridge.links.size() - 1 ? null : PluginBridge.links.get(index + 1);
            if (previous != null && next != null) {
                PluginBridge.bidirectionalBridge(previous.getPluginRoot(), next.getPluginRoot());
            }
            if (previous != null) {
                PluginBridge.removeBidirectional(previous.getPluginRoot(), plugin.getPluginRoot());
            }
            if (next != null) {
                PluginBridge.removeBidirectional(next.getPluginRoot(), plugin.getPluginRoot());
            }
            PluginBridge.links.remove(index);
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
        if (PluginBridge.links.isEmpty()) {
            return Collections.emptyList();
        } else {
            ProdityPlugin head = PluginBridge.links.get(0);

            return (List<InjectionFeature>) head.getServices().getAllServices(new InjectionFeatureFilter(plugin));
        }
    }
}
