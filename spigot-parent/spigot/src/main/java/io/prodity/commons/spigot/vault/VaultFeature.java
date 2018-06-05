package io.prodity.commons.spigot.vault;

import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.SoftDepend;
import io.prodity.commons.plugin.ProdityPlugin;
import net.milkbowl.vault.economy.Economy;
import org.jvnet.hk2.annotations.Service;

@Export
@Service
@SoftDepend("Vault")
public class VaultFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bindFactory(VaultEconomyFactory.class).to(Economy.class);
        });
    }

}