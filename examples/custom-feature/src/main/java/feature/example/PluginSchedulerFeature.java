package feature.example;

import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import org.jvnet.hk2.annotations.Service;

/**
 * By exporting the service, it's available to other plugins.
 * InjectionFeatures are, however, a special case.  InjectionFeatures
 * will only be applied to a plugin if it explicitly depends on the
 * plugin that exports it. This allows plugins to run in a deterministic
 * way, and opt-in to the injection functionality that another plugin provides.
 * <p />
 * InjectionFeatures are created prior to a plugin's services being loaded. This
 * means that for a plugin to use their own InjectionFeatures, they need to override
 * {@link SpigotInjectedPlugin#initialize()}, and provide an explicit binding instead
 * of marking the service with @Service and @Export.
 */
@Export
@Service
public class PluginSchedulerFeature implements InjectionFeature {

    // Provide the binding before loading all the services from the plugin.
    // Since our feature isn't dependent on what they have loaded, we can bind
    // it before they're loaded.  This is additionally useful because some service
    // types are initialized as soon as they're loaded.
    @Override
    public void preLoad(ProdityPlugin plugin) {
        // This has to be bound to every plugin individually, because each plugin should
        // have its own PluginScheduler
        this.bind(plugin, binder -> {
            binder.bind(PluginScheduler.class);
        });
    }
}
