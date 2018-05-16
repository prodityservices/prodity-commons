package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.Platform;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class SpigotPlatform implements Platform {

    @Override
    public boolean isListener(Object instance) {
        return instance instanceof Listener;
    }

    @Override
    public void registerListener(Object instance, ProdityPlugin plugin) {
        Bukkit.getPluginManager().registerEvents((Listener) instance, (SpigotInjectedPlugin) plugin);
    }

    @Override
    public void unregisterListener(Object instance) {
        HandlerList.unregisterAll((Listener) instance);
    }

    @Override
    public boolean hasPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public boolean isEnabled(ProdityPlugin plugin) {
        return Bukkit.getPluginManager().isPluginEnabled(plugin.getName());
    }
}
