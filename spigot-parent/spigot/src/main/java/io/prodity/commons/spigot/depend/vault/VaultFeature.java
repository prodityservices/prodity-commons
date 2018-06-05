package io.prodity.commons.spigot.depend.vault;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.SoftDepend;
import io.prodity.commons.plugin.ProdityPlugin;
import net.milkbowl.vault.economy.Economy;

@SoftDepend("Vault")
public class VaultFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bindFactory(VaultEconomyFactory.class).to(Economy.class);
        });
    }

}