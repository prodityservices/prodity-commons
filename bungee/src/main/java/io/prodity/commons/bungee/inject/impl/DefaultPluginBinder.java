package io.prodity.commons.bungee.inject.impl;

import io.prodity.commons.bungee.inject.BungeeInjectedPlugin;
import io.prodity.commons.inject.bind.PluginBinder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.logging.Logger;

public class DefaultPluginBinder extends PluginBinder {
    private final BungeeInjectedPlugin plugin;
    public DefaultPluginBinder(BungeeInjectedPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(plugin).named(plugin.getName()).to(Plugin.class);
        bind(plugin.getLogger()).to(Logger.class);
        bind(ProxyServer.getInstance()).to(ProxyServer.class);
        bind(ProxyServer.getInstance().getPluginManager()).to(PluginManager.class);
        bind(ProxyServer.getInstance().getScheduler()).to(TaskScheduler.class);

    }
}
