package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Common bindings useful to all plugins.  Added by default.
 */
public class DefaultPluginBinder<T extends SpigotInjectedPlugin> extends PluginBinder {

    private final T plugin;
    private final Class<T> pluginType;

    public DefaultPluginBinder(T plugin, Class<T> pluginType) {
        super(plugin);
        this.plugin = plugin;
        this.pluginType = pluginType;
    }

    @Override
    protected void configure() {
        this.bind(this.plugin).named(this.plugin.getName())
            .to(this.pluginType)
            .to(SpigotInjectedPlugin.class)
            .to(JavaPlugin.class)
            .to(Plugin.class);
        this.bind(this.plugin.getLogger()).to(Logger.class);
        this.bind(Bukkit.getServer()).to(Server.class);
        this.bind(Bukkit.getPluginManager()).to(PluginManager.class);
        this.bind(Bukkit.getMessenger()).to(Messenger.class);
        this.bind(Bukkit.getScheduler()).to(BukkitScheduler.class);
    }


    public static DefaultPluginBinder<?> createFor(SpigotInjectedPlugin plugin) {
        return DefaultPluginBinder.doCreate(plugin.getClass(), plugin);
    }

    private static <T extends SpigotInjectedPlugin> DefaultPluginBinder doCreate(Class<T> tClass, JavaPlugin plugin) {
        return new DefaultPluginBinder<>(tClass.cast(plugin), tClass);
    }
}
