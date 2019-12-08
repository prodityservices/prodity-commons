package io.prodity.commons.bungee.inject.impl;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.CommandManager;
import io.prodity.commons.bungee.inject.CommandManagerCustomizer;
import io.prodity.commons.bungee.plugin.ProdityBungeePlugin;
import io.prodity.commons.command.ProdityCommand;
import io.prodity.commons.inject.InjectionFeature;
import io.prodity.commons.inject.impl.InjectUtils;
import io.prodity.commons.plugin.ProdityPlugin;

import java.util.List;

public class CommandFeature implements InjectionFeature {

	@Override
	public void preEnable(ProdityPlugin plugin) {
		final ProdityBungeePlugin spigotPlugin = (ProdityBungeePlugin) plugin;

		final BungeeCommandManager commandManager = new BungeeCommandManager(spigotPlugin);
		commandManager.enableUnstableAPI("help");

		// bind the command manager prior to loading the commands in-case any commands inject a service
		// that require the command manager as commands are treated as Eager
		this.bind(plugin, binder -> {
			binder.bind(commandManager)
					.named(plugin.getName())
					.to(commandManager.getClass())
					.to(CommandManager.class);
		});

		final List<ProdityCommand> commands = InjectUtils.getLocalServices(plugin, ProdityCommand.class);
		if (!commands.isEmpty()) {
			final List<CommandManagerCustomizer> customizers = InjectUtils.getDependentServices(plugin, CommandManagerCustomizer.class);
			for (CommandManagerCustomizer customizer : customizers) {
				customizer.customize(commandManager);
			}

			for (ProdityCommand command : commands) {
				commandManager.registerCommand(command);
			}
		}
	}

	@Override
	public void preDisable(ProdityPlugin plugin) {
		final BungeeCommandManager commandManager = plugin.getServices().getService(BungeeCommandManager.class);
		if (commandManager != null) {
			commandManager.unregisterCommands();
		}
	}

}
