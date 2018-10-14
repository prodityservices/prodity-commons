package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.inject.impl.Platform;
import io.prodity.commons.inject.impl.ProdityInjectionResolver;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import io.prodity.commons.spigot.thread.Threads;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;

import javax.inject.Inject;
import java.util.logging.Logger;

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
		this.bind(Bukkit.getServicesManager()).to(ServicesManager.class);
		this.bind(SpigotPlatform.class).to(Platform.class);
		this.bind(ProdityInjectionResolver.class).ranked(10).to(new TypeLiteral<InjectionResolver<Inject>>() {
		});
		this.bind(Threads.class).to(io.prodity.commons.threads.Threads.class);
	}

}
