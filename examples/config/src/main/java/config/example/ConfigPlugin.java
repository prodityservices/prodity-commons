package config.example;

import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import javax.inject.Inject;

@Plugin(name = "ExampleConfig", version = "1.0.0", description = "Displays example usages of the commons config system")
@PluginDependency("ProdityCommons")
public class ConfigPlugin extends ProditySpigotPlugin {

    //Injecting the config so it is automatically injected on the plugin startup for this example.
    @Inject
    private ExampleConfig exampleConfig;

}