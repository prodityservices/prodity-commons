package io.prodity.commons.spigot.legacy.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface PluginListener extends Listener {

    Plugin getPlugin();

    default void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
    }

    default void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

}