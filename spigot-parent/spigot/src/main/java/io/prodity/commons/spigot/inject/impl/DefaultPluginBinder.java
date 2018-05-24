package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Common bindings useful to all plugins.  Added byWithType default.
 */
public class DefaultPluginBinder extends PluginBinder {

    private final SpigotInjectedPlugin plugin;

    public DefaultPluginBinder(SpigotInjectedPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(this.plugin).named(this.plugin.getName())
            .to(ProditySpigotPlugin.class)
            .to(SpigotInjectedPlugin.class)
            .to(JavaPlugin.class)
            .to(Plugin.class)
            .to(ProdityPlugin.class);
        this.bind(this.plugin.getLogger()).to(Logger.class);
        this.bind(Bukkit.getServer()).to(Server.class);
        this.bind(Bukkit.getPluginManager()).to(PluginManager.class);
        this.bind(Bukkit.getMessenger()).to(Messenger.class);
        this.bind(Bukkit.getScheduler()).to(BukkitScheduler.class);
    }

}
