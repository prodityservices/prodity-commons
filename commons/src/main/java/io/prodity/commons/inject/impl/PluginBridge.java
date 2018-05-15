package io.prodity.commons.inject.impl;

import io.prodity.commons.plugin.ProdityPlugin;
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
            PluginBridge.bidirectionalBridge(tail.getServices(), plugin.getServices());
        }
    }

    public static void unbridge(ProdityPlugin plugin) {
        int index = PluginBridge.links.indexOf(plugin);
        if (index != -1) {
            ProdityPlugin previous = index == 0 ? null : PluginBridge.links.get(index - 1);
            ProdityPlugin next = index == PluginBridge.links.size() - 1 ? null : PluginBridge.links.get(index + 1);
            if (previous != null && next != null) {
                PluginBridge.bidirectionalBridge(previous.getServices(), next.getServices());
            }
            if (previous != null) {
                PluginBridge.removeBidirectional(previous.getServices(), plugin.getServices());
            }
            if (next != null) {
                PluginBridge.removeBidirectional(next.getServices(), plugin.getServices());
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
}
