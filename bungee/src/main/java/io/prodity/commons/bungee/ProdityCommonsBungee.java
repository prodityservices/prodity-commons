package io.prodity.commons.bungee;

import io.prodity.commons.bungee.plugin.ProdityBungeePlugin;
import io.prodity.commons.bungee.plugin.annotate.PluginAuthor;
import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;

@Plugin(name = "ProdityCommons", description = "Core utilities for Prodity bungee plugins", version = "%plugin.version%")
@PluginAuthor("FakeNeth, Nate Mortensen")
@PluginDependency(value = "ABungeePlugin", soft = true)
@PluginDependency(value = "AnotherBungeePlugin")
public class ProdityCommonsBungee extends ProdityBungeePlugin {

}