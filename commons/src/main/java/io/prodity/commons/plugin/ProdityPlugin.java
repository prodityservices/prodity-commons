package io.prodity.commons.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Logger;
import org.glassfish.hk2.api.ServiceLocator;

/**
 * Abstraction over both Bukkit and Bungee plugins.
 */
public interface ProdityPlugin {

    File getDataFolder();

    InputStream getResource(String fileName);

    String getName();

    ServiceLocator getServices();

    Set<String> getDependencies();

    Set<String> getSoftDependencies();

    Logger getLogger();
}