package io.prodity.commons.spigot;

import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import io.prodity.commons.spigot.plugin.annotate.PluginAuthor;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadBefore;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadState;
import io.prodity.commons.spigot.plugin.annotate.PluginLoadState.LoadState;
import io.prodity.commons.spigot.plugin.annotate.PluginWebsite;

@Plugin(name = "ProdityCommons", description = "Core utilities for Prodity spigot plugins", version = "%plugin.version%")
@PluginWebsite("prodity.io")
@PluginAuthor("FakeNeth")
@PluginAuthor("Nate Mortensen")
@PluginDependency(value = "PlaceholderAPI", soft = true)
@PluginDependency(value = "ProtocolLib")
@PluginLoadState(LoadState.POSTWORLD)
@PluginLoadBefore("SomePluginToLoadBefore1")
@PluginLoadBefore("AnotherPluginToLoadBefore")
public class ProdityCommonsSpigot extends ProditySpigotPlugin {

}