package io.prodity.commons.spigot.thread;

import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import org.jvnet.hk2.annotations.Service;

@Service
@Export
public class ThreadsFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bind(Threads.class);
        });
    }

}