package io.prodity.commons.bungee.plugin;

import io.prodity.commons.plugin.ProdityPlugin;
import java.io.InputStream;
import java.util.Set;

import net.md_5.bungee.api.plugin.Plugin;
import org.glassfish.hk2.api.ServiceLocator;

public class ProdityBungeePlugin extends Plugin implements ProdityPlugin {

    @Override
    public InputStream getResource(String fileName) {
        return this.getResourceAsStream(fileName);
    }

    @Override
    public String getName() {
        return this.getDescription().getName();
    }

    @Override
    public ServiceLocator getServices() {
        return null;
    }

    @Override
    public ServiceLocator getPluginRoot() {
        return null;
    }

    @Override
    public Set<String> getDependencies() {
        return this.getDescription().getDepends();
    }

    @Override
    public Set<String> getSoftDependencies() {
        return this.getDescription().getSoftDepends();
    }

}