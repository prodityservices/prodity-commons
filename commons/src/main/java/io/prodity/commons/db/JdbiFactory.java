package io.prodity.commons.db;

import com.google.common.base.Preconditions;
import io.prodity.commons.config.inject.ConfigInjector;
import io.prodity.commons.config.inject.except.ConfigInjectException;
import io.prodity.commons.lazy.Lazy;
import io.prodity.commons.lazy.SimpleLazy;
import io.prodity.commons.plugin.ProdityPlugin;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.hk2.api.Visibility;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;


public class JdbiFactory implements Factory<Jdbi> {

    private final Lazy<Jdbi> instance = new SimpleLazy<>(this::create);
    private final IterableProvider<JdbiFeature> features;
    private final Provider<ConfigInjector> injector;
    private final ProdityPlugin plugin;

    @Inject
    public JdbiFactory(IterableProvider<JdbiFeature> features, Provider<ConfigInjector> injector, ProdityPlugin plugin) {
        this.features = features;
        this.injector = injector;
        this.plugin = plugin;
    }

    private Jdbi create() {
        try {
            DataSource source = this.createDataSource();
            this.performMigrations(source);

            Jdbi jdbi = Jdbi.create(source);
            jdbi.installPlugin(new SqlObjectPlugin());
            // Now install from the ServiceLocator
            for (JdbiFeature feature : this.features) {
                jdbi.installPlugin(feature);
            }
            return jdbi;
        } catch (ConfigInjectException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource createDataSource() throws ConfigInjectException {
        DatabaseConfig config = this.injector.get().inject(DatabaseConfig.class);
        return config.createDataSource(this.plugin);
    }

    private void performMigrations(DataSource dataSource) {
        Flyway migrations = new Flyway(this.plugin.getClass().getClassLoader());
        migrations.setBaselineVersion(MigrationVersion.fromVersion("0"));
        migrations.setTable(JdbiFactory.toAlphaNumeric(this.plugin.getName())+"_schema_history");
        migrations.setDataSource(dataSource);
        migrations.setBaselineOnMigrate(true);
        this.plugin.getLogger().info("Checking for database migrations...");
        migrations.migrate();
        this.plugin.getLogger().info("Finished migrations.");
    }

    private static String toAlphaNumeric(String input) {
        return input.toLowerCase().replace('-', '_').replaceAll("[^a-z0-9_]", "");
    }

    @Singleton
    @Visibility(DescriptorVisibility.LOCAL)
    @Override
    public Jdbi provide() {
        return Preconditions.checkNotNull(this.instance.get());
    }

    @Override
    public void dispose(Jdbi instance) {
    }
}
