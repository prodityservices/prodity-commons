package io.prodity.commons.inject.impl;

import io.prodity.commons.plugin.ProdityPlugin;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Platform {
    boolean isListener(Object instance);
    void registerListener(Object instance, ProdityPlugin plugin);
    void unregisterListener(Object instance);
    boolean hasPlugin(String name);
    boolean isEnabled(ProdityPlugin plugin);
}
