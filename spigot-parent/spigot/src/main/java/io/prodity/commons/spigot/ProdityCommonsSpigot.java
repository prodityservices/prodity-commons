package io.prodity.commons.spigot;

import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.spigot.inject.impl.InternalBinder;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import io.prodity.commons.spigot.plugin.annotate.PluginAuthor;
import io.prodity.commons.spigot.plugin.annotate.PluginWebsite;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

@Plugin(name = "ProdityCommons", description = "Core utilities for Prodity spigot plugins", version = "%plugin.version%")
@PluginWebsite("prodity.io")
@PluginAuthor("FakeNeth")
@PluginAuthor("Nate Mortensen")
@PluginDependency(value = "PlaceholderAPI", soft = true)
@PluginDependency(value = "ProtocolLib")
public class ProdityCommonsSpigot extends ProditySpigotPlugin {

    @Override
    protected ServiceLocator initialize(ServiceLocator locator) {
        super.initialize(locator);
        // Export all the core features
        ServiceLocatorUtilities.bind(locator, new InternalBinder(this));
        return locator;
    }

}