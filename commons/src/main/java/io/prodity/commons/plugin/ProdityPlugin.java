package io.prodity.commons.plugin;

import org.glassfish.hk2.api.ServiceLocator;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

/**
 * Abstraction over both Bukkit and Bungee plugins.
 */
public interface ProdityPlugin {

    File getDataFolder();

    InputStream getResource(String fileName);

    String getName();

    ServiceLocator getServices();

    ServiceLocator getPluginRoot();

    Set<String> getDependencies();

    Set<String> getSoftDependencies();

}