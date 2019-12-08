package io.prodity.commons.bungee;

import io.prodity.commons.bungee.inject.impl.InternalBinder;
import io.prodity.commons.bungee.plugin.ProdityBungeePlugin;
import io.prodity.commons.bungee.plugin.annotate.PluginAuthor;
import io.prodity.commons.plugin.annotate.Plugin;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

@Plugin(name = "ProdityCommonsBungee", description = "Core utilities for Prodity bungee plugins", version = "%plugin.version%")
@PluginAuthor("FakeNeth, Nate Mortensen")
public class ProdityCommonsBungee extends ProdityBungeePlugin {

	@Override
	protected void initialize() {
		super.initialize();

		// Export all the core features
		ServiceLocatorUtilities.bind(this.getServices(), new InternalBinder(this));
	}

}