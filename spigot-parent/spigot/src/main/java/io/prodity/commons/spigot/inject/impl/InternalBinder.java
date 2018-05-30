package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.impl.CoreBinder;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.db.SpigotDatabaseFeature;

/**
 * This is the PluginBinder that Commons uses to export all the default features
 */
public class InternalBinder extends CoreBinder {

    public InternalBinder(ProdityPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void configure() {
        super.configure();
        this.addCoreFeatures(McVersionFeature.class, TaskFeature.class, CommandFeature.class, SpigotDatabaseFeature.class);
    }

}