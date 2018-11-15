package io.prodity.commons.bungee.inject.impl;

import co.aikar.commands.BungeeCommandManager;
import io.prodity.commons.bungee.inject.CommandManagerCustomizer;
import io.prodity.commons.bungee.plugin.ProdityBungeePlugin;
import io.prodity.commons.command.ProdityCommand;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.plugin.ProdityPlugin;

import java.util.List;

public class CommandFeature implements InjectionFeature {

	@Override
	public void onEnable(ProdityPlugin plugin) {
		ProdityBungeePlugin spigotPlugin = (ProdityBungeePlugin) plugin;
		List<ProdityCommand> commands = InjectUtils.getLocalServices(plugin, ProdityCommand.class);

		if (!commands.isEmpty()) {
			BungeeCommandManager manager = new BungeeCommandManager(spigotPlugin);
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
