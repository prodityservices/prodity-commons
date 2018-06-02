package io.prodity.commons.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.config.annotate.inject.ConfigInject;
import io.prodity.commons.config.annotate.inject.Required;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Config(fileName = "database.yml")
@Singleton
public class DatabaseConfig {

    @ConfigInject
    @Required
    private String host;

    @ConfigInject
    private final int port = 3306;

    @ConfigInject
    @Required
    private String database;

    @ConfigInject
    private String username;

    @ConfigInject
    private String password;

    public DataSource createDataSource(ProdityPlugin plugin) {
        HikariConfig config = this.createConfig(plugin);
        return new HikariDataSource(config);
    }

    private HikariConfig createConfig(ProdityPlugin plugin) {
        HikariConfig config = new HikariConfig();
        config.addDataSourceProperty("serverName", this.host);
        config.addDataSourceProperty("port", this.port);
        config.addDataSourceProperty("databaseName", this.database);
        config.setMaximumPoolSize(25);
        config.setPoolName(plugin.getName() + "-database");
        config.setDataSourceClassName("org.mariadb.jdbc.MySQLDataSource");
        config.addDataSourceProperty("properties", "useUnicode=true;characterEncoding=utf8");
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(15L));
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10L));

        if (this.username != null) {
            config.addDataSourceProperty("user", this.username);
        }

        if (this.password != null) {
            config.addDataSourceProperty("password", this.password);
        }

        return config;
    }

}