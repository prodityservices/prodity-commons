package io.prodity.commons.spigot;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.bukkit.Bukkit;
import org.jvnet.hk2.annotations.Service;

@Service
public class TestFeature implements InjectionFeature {

    @Inject
    private Logger logger;

    @Override
    public void postEnable(ProdityPlugin plugin) {
        this.logger.info("ITS WORKING.");
        Bukkit.broadcastMessage("GOOD.");
    }

}