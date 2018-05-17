package io.prodity.commons.config.inject;

import io.prodity.commons.inject.Export;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.plugin.ProdityPlugin;
import org.jvnet.hk2.annotations.Service;

@Export
@Service
public class ConfigInjectorFeature implements InjectionFeature {

    @Override
    public void preLoad(ProdityPlugin plugin) {
        this.bind(plugin, (binder) -> {
            binder.bind(SimpleConfigInjector.class).to(ConfigInjector.class);
        });
    }

}