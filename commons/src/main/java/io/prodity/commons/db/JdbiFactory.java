package io.prodity.commons.db;

import com.google.common.base.Preconditions;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.lazy.Lazy;
import io.prodity.commons.lazy.SimpleLazy;
import io.prodity.commons.plugin.ProdityPlugin;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.glassfish.hk2.api.DescriptorVisibility;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Visibility;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.gson2.Gson2Plugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;


public class JdbiFactory implements Factory<Jdbi> {

    private static String toAlphaNumeric(String input) {
        return input.toLowerCase().replace('-', '_').replaceAll("[^a-z0-9_]", "");
    }

    private final ProdityPlugin plugin;
    private final DatabaseConfig databaseConfig;
    private final Lazy<Jdbi> instance = new SimpleLazy<>(this::create);

    @Inject
    public JdbiFactory(ProdityPlugin plugin, DatabaseConfig config) {
        this.plugin = plugin;
        this.databaseConfig = config;
    }

    private Jdbi create() {
        DataSource source = this.createDataSource();
        this.performMigrations(source);

        Jdbi jdbi = Jdbi.create(source);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new Gson2Plugin());
        // Now install from the ServiceLocator
        for (JdbiCustomizer feature : InjectUtils.getDependentServices(this.plugin, JdbiCustomizer.class)) {
            jdbi.installPlugin(feature);
        }
        return jdbi;
    }

    private DataSource createDataSource() {
        return this.databaseConfig.createDataSource(this.plugin);
    }

    private void performMigrations(DataSource dataSource) {
        Flyway migrations = new Flyway(this.plugin.getClass().getClassLoader());
        migrations.setBaselineVersion(MigrationVersion.fromVersion("0"));
        migrations.setTable(JdbiFactory.toAlphaNumeric(this.plugin.getName()) + "_schema_history");
        migrations.setDataSource(dataSource);
        migrations.setBaselineOnMigrate(true);
        this.plugin.getLogger().info("Checking for database migrations...");
        migrations.migrate();
        this.plugin.getLogger().info("Finished migrations.");
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
