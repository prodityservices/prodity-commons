package io.prodity.commons.db;

import io.prodity.commons.db.uuid.UUIDCustomizer;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.jdbi.v3.core.Jdbi;

public class DatabaseFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, binder -> {
            binder.bind(UUIDCustomizer.class).to(JdbiCustomizer.class);
            binder.bindFactory(JdbiFactory.class).to(Jdbi.class);
            binder.bind(DatabaseResolver.class).to(JustInTimeInjectionResolver.class);
        });
    }

}
