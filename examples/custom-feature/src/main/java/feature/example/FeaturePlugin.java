package feature.example;

import io.prodity.commons.plugin.annotate.Plugin;
import io.prodity.commons.plugin.annotate.PluginDependency;
import io.prodity.commons.spigot.plugin.ProditySpigotPlugin;
import javax.inject.Inject;

@Plugin(name = "ExampleCustomFeature", version = "1.0.0", description = "Allows plugins to inject a PluginScheduler")
@PluginDependency("ProdityCommons")
public class FeaturePlugin extends ProditySpigotPlugin {

    @Inject
    private ConfigTest configTest;

}
