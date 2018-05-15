package io.prodity.commons.bungee.inject.impl;

import io.prodity.commons.bungee.inject.BungeeInjectedPlugin;
import io.prodity.commons.inject.impl.Platform;
import io.prodity.commons.plugin.ProdityPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeePlatform implements Platform {
    @Override
    public boolean isListener(Object instance) {
        return instance instanceof Listener;
    }

    @Override
    public void registerListener(Object instance, ProdityPlugin plugin) {
        BungeeInjectedPlugin bungeePlugin = (BungeeInjectedPlugin) plugin;
        this.getPluginManager().registerListener(bungeePlugin, (Listener) instance);
    }

    @Override
    public void unregisterListener(Object instance) {
        this.getPluginManager().unregisterListener((Listener) instance);
    }

    @Override
    public boolean hasPlugin(String name) {
        return this.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public boolean isEnabled(ProdityPlugin plugin) {
        return true;
    }

    private PluginManager getPluginManager() {
        return ProxyServer.getInstance().getPluginManager();
    }
}
