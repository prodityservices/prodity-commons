package io.prodity.commons.inject.bind;

import io.prodity.commons.plugin.ProdityPlugin;

import java.util.function.Consumer;

/**
 * Utility class for creating bindings by applying a consumer
 * to the pluginbinder.
 */
public class ConsumerBinder extends PluginBinder {
    private final ProdityPlugin plugin;
    private final Consumer<PluginBinder> consumer;

    public ConsumerBinder(ProdityPlugin plugin, Consumer<PluginBinder> consumer) {
        super(plugin);
        this.plugin = plugin;
        this.consumer = consumer;
    }

    @Override
    protected void configure() {
        this.consumer.accept(this);
    }
}
