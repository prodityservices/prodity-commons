package io.prodity.commons.spigot.inject.impl;

import io.prodity.commons.inject.SoftDepend;
import io.prodity.commons.inject.impl.AnnotationProcessor;
import org.bukkit.plugin.PluginManager;
import org.glassfish.hk2.utilities.DescriptorImpl;

import javax.inject.Inject;


public class SoftDependProcessor extends AnnotationProcessor<SoftDepend> {
    private final PluginManager pluginManager;

    @Inject
    public SoftDependProcessor(PluginManager pluginManager) {
        super(SoftDepend.class);
        this.pluginManager = pluginManager;
    }

    @Override
    protected DescriptorImpl doProcess(DescriptorImpl descriptor, SoftDepend value) {
        return isSatisfied(value) ? descriptor : null;
    }

    private boolean isSatisfied(SoftDepend depend) {
        for (String plugin : depend.value()) {
            if (pluginManager.getPlugin(plugin) == null) {
                return false;
            }
        }
        return true;
    }
}
