package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import io.prodity.commons.inject.bind.PluginBinder;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

/**
 * Common bindings useful to all plugins.  Added by default.
 * @param <T>
 */
public class DefaultPluginBinder<T extends SpigotInjectedPlugin> extends PluginBinder
{
    private final T plugin;
    private final Class<T> pluginType;

    public DefaultPluginBinder(T plugin, Class<T> pluginType)
    {
        super(plugin);
        this.plugin = plugin;
        this.pluginType = pluginType;
    }

    protected void configure()
    {
        bind(plugin).named(plugin.getName())
                .to(pluginType)
                .to(SpigotInjectedPlugin.class)
                .to(JavaPlugin.class)
                .to(Plugin.class);
        bind(plugin.getLogger()).to(Logger.class);
        bind(Bukkit.getServer()).to(Server.class);
        bind(Bukkit.getPluginManager()).to(PluginManager.class);
        bind(Bukkit.getMessenger()).to(Messenger.class);
        bind(Bukkit.getScheduler()).to(BukkitScheduler.class);
    }


    public static DefaultPluginBinder<?> createFor(SpigotInjectedPlugin plugin)
    {
        return doCreate(plugin.getClass(), plugin);
    }

    private static <T extends SpigotInjectedPlugin> DefaultPluginBinder doCreate(Class<T> tClass, JavaPlugin plugin)
    {
        return new DefaultPluginBinder<>(tClass.cast(plugin), tClass);
    }
}
