package io.prodity.commons.spigot.depend.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import javax.inject.Inject;
import org.bukkit.plugin.ServicesManager;
import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

@Service
public class ProtocolManagerFactory implements Factory<ProtocolManager> {

    @Inject
    private ServicesManager servicesManager;

    @Override
    public ProtocolManager provide() {
        return ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void dispose(ProtocolManager instance) {
        //noop
    }

}