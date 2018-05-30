package io.prodity.commons.spigot.db;

import io.prodity.commons.db.JdbiCustomizer;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.db.itemmeta.ItemMetaCustomizer;

public class SpigotDatabaseFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, binder -> {
            binder.bind(ItemMetaCustomizer.class).to(JdbiCustomizer.class);
        });
    }

}