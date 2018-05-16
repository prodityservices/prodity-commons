package io.prodity.commons.inject;

import io.prodity.commons.inject.bind.ConsumerBinder;
import io.prodity.commons.inject.bind.PluginBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.function.Consumer;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jvnet.hk2.annotations.Contract;

/**
 * InjectionFeatures are the core of PluginInject.  Nearly all
 * functionality is implemented through them.  Plugins can add
 * their own, or remove added InjectionFeatures by overriding
 * their platform-specific initialize method.
 * <p />
 * Plugins can provide InjectionFeatures for other plugins
 * by exporting them.  Plugins which explicitly depend or soft depend on
 * another plugin will have all InjectionFeatures
 * that the plugin exports applied to it.  For example, if plugin A exports
 * an InjectionFeature, and plugin B depends on A, the InjectionFeature will
 * be applied to B.  Unrelated plugin C will not have the InjectionFeature
 * applied, regardless of their initialization order.  For more information
 * on exporting services, see {@link Export}.
 * <p />
 * Special care should be taken if a plugin relies on an InjectionFeature from
 * a soft dependency.  Ideally any services that use that functionality should
 * be marked with {@link SoftDepend} or be
 * so abstracted from the feature that they won't fail to resolveValues without it.
 */
@Contract
public interface InjectionFeature {

    /**
     * Invoked prior to plugin configuration being loaded
     *
     * @param plugin the plugin
     */
    default void preLoad(ProdityPlugin plugin) {
    }

    /**
     * Invoked after plugin configuration has been loaded
     *
     * @param plugin non-null plugin ServiceLocator
     */
    default void postLoad(ProdityPlugin plugin) {
    }

    /**
     * Invoked after the plugin has been enabled, but
     * prior to Eager initialization.
     *
     * @param plugin the plugin
     */
    default void preEnable(ProdityPlugin plugin) {
    }

    /**
     * Performs initialization logic.
     * @param plugin the plugin
     */
    default void onEnable(ProdityPlugin plugin) {
    }

    /**
     * Invoked after eager initialization.
     *
     * @param plugin the plugin
     */
    default void postEnable(ProdityPlugin plugin) {
    }

    /**
     * Invoked prior to ServiceLocator shutdown.
     *
     * @param plugin the plugin
     */
    default void preDisable(ProdityPlugin plugin) {
    }

    default void bind(ProdityPlugin plugin, Consumer<PluginBinder> binder) {
        ServiceLocatorUtilities.bind(plugin.getServices(), new ConsumerBinder(plugin, binder));
    }
}
