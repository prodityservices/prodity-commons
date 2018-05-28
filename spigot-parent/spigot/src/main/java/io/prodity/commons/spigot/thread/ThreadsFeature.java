package io.prodity.commons.spigot.thread;

import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;

public class ThreadsFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bind(Threads.class);
        });
    }

}