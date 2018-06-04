package io.prodity.commons.spigot.inject.impl;

import co.aikar.commands.BukkitCommandManager;
import io.prodity.commons.command.ProdityCommand;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.plugin.ProdityPlugin;
import io.prodity.commons.spigot.inject.CommandManagerCustomizer;
import io.prodity.commons.spigot.inject.SpigotInjectedPlugin;
import java.util.List;

public class CommandFeature implements InjectionFeature {

    @Override
    public void onEnable(ProdityPlugin plugin) {
        SpigotInjectedPlugin spigotPlugin = (SpigotInjectedPlugin) plugin;
        List<ProdityCommand> commands = InjectUtils.getLocalServices(plugin, ProdityCommand.class);

        if (!commands.isEmpty()) {
            BukkitCommandManager manager = new BukkitCommandManager(spigotPlugin);
            manager.enableUnstableAPI("help");
            List<CommandManagerCustomizer> customizers = InjectUtils.getDependentServices(plugin, CommandManagerCustomizer.class);
            for (CommandManagerCustomizer customizer : customizers) {
                customizer.customize(manager);
            }
            for (ProdityCommand command : commands) {
                manager.registerCommand(command);
            }
            this.bind(plugin, binder -> {
                binder.bind(manager);
            });
        }
    }
}
