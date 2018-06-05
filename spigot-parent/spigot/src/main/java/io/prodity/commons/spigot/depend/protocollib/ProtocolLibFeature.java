package io.prodity.commons.spigot.depend.protocollib;

import com.comphenix.protocol.ProtocolManager;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.SoftDepend;
import io.prodity.commons.plugin.ProdityPlugin;

@SoftDepend("ProtocolLib")
public class ProtocolLibFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bindFactory(ProtocolManagerFactory.class).to(ProtocolManager.class);
        });
    }

}