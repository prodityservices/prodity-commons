package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.Platform;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class SpigotPlatform implements Platform {

    private final SpigotInjectedPlugin plugin;

    @Inject
    public SpigotPlatform(SpigotInjectedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isListener(Object instance) {
        return instance instanceof Listener;
    }

    @Override
    public void registerListener(Object instance) {
        Bukkit.getPluginManager().registerEvents((Listener) instance, this.plugin);
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
    public boolean isEnabled() {
        return this.plugin.isEnabled();
    }

    @Override
    public Executor getAsyncExecutor() {
        return runnable -> Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
}
