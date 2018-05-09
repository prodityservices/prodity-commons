package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.DescriptorProcessor;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import org.bukkit.Bukkit;

public class McVersionFeature implements InjectionFeature {

    public static String CURRENT = Bukkit.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit.v", "");

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, binder -> {
            binder.bind(McVersionProcessor.class).to(DescriptorProcessor.class);
        });
    }
}
