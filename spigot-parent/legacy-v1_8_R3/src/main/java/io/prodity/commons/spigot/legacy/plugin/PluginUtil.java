package io.prodity.commons.spigot.legacy.plugin;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import org.bukkit.plugin.java.JavaPlugin;

public enum PluginUtil {

    ;

    private static final LazyValue<JavaPlugin> PLUGIN_LAZY = new SimpleLazyValue<>(PluginUtil::findPlugin);

    public static JavaPlugin getProvidingPlugin() {
        return PluginUtil.PLUGIN_LAZY.get();
    }

    private static JavaPlugin findPlugin() {
        return JavaPlugin.getProvidingPlugin(PluginUtil.class);
    }

}