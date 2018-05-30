package io.prodity.commons.bungee.inject.impl;

import io.prodity.commons.bungee.inject.BungeeInjectedPlugin;
import io.prodity.commons.inject.impl.Platform;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeePlatform implements Platform {

    private final BungeeInjectedPlugin plugin;

    @Inject
    public BungeePlatform(BungeeInjectedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isListener(Object instance) {
        return instance instanceof Listener;
    }

    @Override
    public void registerListener(Object instance) {
        this.getPluginManager().registerListener(this.plugin, (Listener) instance);
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
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Executor getAsyncExecutor() {
        return runnable -> ProxyServer.getInstance().getScheduler().runAsync(this.plugin, runnable);
    }

    private PluginManager getPluginManager() {
        return ProxyServer.getInstance().getPluginManager();
    }
}
