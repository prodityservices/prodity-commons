package io.prodity.commons.spigot.depend.vault;

import javax.inject.Inject;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicesManager;
import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

@Service
public class VaultEconomyFactory implements Factory<Economy> {

    @Inject
    private ServicesManager servicesManager;

    @Override
    public Economy provide() {
        return this.servicesManager.getRegistration(Economy.class).getProvider();
    }

    @Override
    public void dispose(Economy instance) {
        //noop
    }

}