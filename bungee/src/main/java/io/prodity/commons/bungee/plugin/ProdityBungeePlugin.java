package io.prodity.commons.bungee.plugin;

import io.prodity.commons.plugin.ProdityPlugin;
import java.io.InputStream;
import net.md_5.bungee.api.plugin.Plugin;

public class ProdityBungeePlugin extends Plugin implements ProdityPlugin {

    @Override
    public InputStream getResource(String fileName) {
        return this.getResourceAsStream(fileName);
    }

}