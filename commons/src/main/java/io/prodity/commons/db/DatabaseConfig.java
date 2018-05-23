package io.prodity.commons.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.prodity.commons.config.annotate.inject.Config;
import io.prodity.commons.plugin.ProdityPlugin;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

//TODO Make this work
public class DatabaseConfig {
    private String host = "localhost";
    private int port = 3306;
    private String database = "test";
    private String username = "nate";
    private String password = "password";

    public DataSource createDataSource(ProdityPlugin plugin) {
        HikariConfig config = this.createConfig(plugin);
        return new HikariDataSource(config);
    }

    private HikariConfig createConfig(ProdityPlugin plugin) {
        // This is all copied from FairytaleUtilities
        HikariConfig config = new HikariConfig();
        config.addDataSourceProperty("serverName", this.host);
        config.addDataSourceProperty("port", this.port);
        config.addDataSourceProperty("databaseName", this.database);
        config.setMaximumPoolSize(25);
        config.setPoolName(plugin.getName()+"-database");
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
